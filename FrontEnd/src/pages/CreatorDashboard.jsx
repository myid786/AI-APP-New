import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Sparkles, TableProperties, UploadCloud } from 'lucide-react'
import API, { getUser } from '../api'

export default function CreatorDashboard() {
  const navigate = useNavigate()
  const user = getUser()
  const [file, setFile] = useState(null)
  const [preview, setPreview] = useState('')
  const [post, setPost] = useState({ title: '', caption: '', location: '', peopleTags: '' })
  const [aiPlan, setAiPlan] = useState(null)
  const [aiMatrix, setAiMatrix] = useState(null)
  const [loading, setLoading] = useState(false)
  const [matrixLoading, setMatrixLoading] = useState(false)
  const [message, setMessage] = useState('')

  if (!user?.userId) {
    return <div className="state-card">Please login as CREATOR to upload media.</div>
  }

  if (user.role !== 'CREATOR') {
    return <div className="state-card error-box">Only CREATOR users can upload media.</div>
  }

  const onFile = (e) => {
    const selected = e.target.files[0]
    setFile(selected)
    if (selected) setPreview(URL.createObjectURL(selected))
  }

  const suggestCaption = async () => {
    setMessage('')
    try {
      const res = await API.post('/api/ai/content-plan', {
        title: post.title,
        location: post.location,
        peopleTags: post.peopleTags,
        mediaType: file?.type?.startsWith('video') ? 'video' : 'image',
        mood: 'fresh'
      })
      setAiPlan(res.data)
      setPost({ ...post, caption: `${res.data.caption} ${res.data.hashtags.join(' ')}` })
    } catch {
      setMessage('AI service not available yet. Check backend deployment.')
    }
  }

  const suggestMatrix = async () => {
    setMessage('')
    setMatrixLoading(true)
    try {
      const res = await API.post('/api/ai/suggestion-matrix', {
        title: post.title,
        caption: post.caption,
        location: post.location,
        peopleTags: post.peopleTags,
        mediaType: file?.type?.startsWith('video') ? 'video' : 'image',
        mood: 'fresh'
      })
      setAiMatrix(res.data)
    } catch {
      setMessage('AI matrix is not available yet. Check backend deployment.')
    } finally {
      setMatrixLoading(false)
    }
  }

  const submit = async (e) => {
    e.preventDefault()
    if (!file) return setMessage('Please choose an image or video.')
    setLoading(true)
    setMessage('')
    try {
      const formData = new FormData()
      formData.append('file', file)

      const uploadRes = await API.post('/api/media/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      })

      await API.post('/api/posts', {
        creatorId: user.userId,
        creatorUsername: user.username,
        title: post.title,
        caption: post.caption,
        location: post.location,
        peopleTags: post.peopleTags,
        mediaUrl: uploadRes.data.url,
        mediaType: uploadRes.data.type
      })

      navigate('/')
    } catch (err) {
      setMessage(err.response?.data?.message || 'Upload failed. Check media-service and post-service.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <section className="creator-layout">
      <form className="creator-form" onSubmit={submit}>
        <p className="eyebrow">Creator View</p>
        <h1>Upload new media</h1>
        {message && <div className="error-box">{message}</div>}

        <label>Title</label>
        <input value={post.title} onChange={(e) => setPost({ ...post, title: e.target.value })} placeholder="Manchester evening walk" required />

        <label>Location</label>
        <input value={post.location} onChange={(e) => setPost({ ...post, location: e.target.value })} placeholder="Manchester, UK" />

        <label>People Present / Tags</label>
        <input value={post.peopleTags} onChange={(e) => setPost({ ...post, peopleTags: e.target.value })} placeholder="zaheer, shahed" />

        <label>Caption</label>
        <textarea value={post.caption} onChange={(e) => setPost({ ...post, caption: e.target.value })} placeholder="Write caption or use AI suggestion..." />

        <div className="ai-actions">
          <button type="button" className="secondary-btn" onClick={suggestCaption}><Sparkles size={17} />AI Content Plan</button>
          <button type="button" className="secondary-btn" onClick={suggestMatrix} disabled={matrixLoading}>
            <TableProperties size={17} />{matrixLoading ? 'Checking...' : 'AI Matrix'}
          </button>
        </div>

        {aiPlan && (
          <div className="ai-plan">
            <strong>{aiPlan.hook}</strong>
            <span>{aiPlan.bestTimeToPost}</span>
            <small>{aiPlan.contentSafetyNote}</small>
          </div>
        )}

        {aiMatrix && (
          <div className="ai-matrix">
            <div className="matrix-head">
              <div>
                <span>AI Matrix Score</span>
                <strong>{aiMatrix.overallScore}/100</strong>
              </div>
              <div className="matrix-verdict">
                <p>{aiMatrix.verdict}</p>
                <span>{aiMatrix.aiProvider}</span>
              </div>
            </div>

            {aiMatrix.azureSentiment && (
              <div className="azure-ai-proof">
                <strong>Azure AI Language sentiment: {aiMatrix.azureSentiment}</strong>
                <span>
                  Positive {Math.round((aiMatrix.azureConfidenceScores?.positive || 0) * 100)}% ·
                  Neutral {Math.round((aiMatrix.azureConfidenceScores?.neutral || 0) * 100)}% ·
                  Negative {Math.round((aiMatrix.azureConfidenceScores?.negative || 0) * 100)}%
                </span>
              </div>
            )}

            <div className="matrix-list">
              {aiMatrix.matrix.map((row) => (
                <div className="matrix-row" key={row.metric}>
                  <div>
                    <strong>{row.metric}</strong>
                    <span>{row.suggestion}</span>
                  </div>
                  <div className={`score-pill ${row.status.toLowerCase().replace(' ', '-')}`}>
                    {row.score}
                  </div>
                </div>
              ))}
            </div>

            <div className="next-actions">
              {aiMatrix.nextActions.map((action) => <span key={action}>{action}</span>)}
            </div>
          </div>
        )}

        <label className="upload-box">
          <UploadCloud size={28} />
          <span>{file ? file.name : 'Choose image or video'}</span>
          <input type="file" accept="image/*,video/*" onChange={onFile} hidden />
        </label>

        <button className="primary-btn full" disabled={loading}>{loading ? 'Uploading...' : 'Publish Post'}</button>
      </form>

      <div className="preview-panel">
        <h3>Live Preview</h3>
        {preview ? (
          file?.type?.startsWith('video') ? <video src={preview} controls /> : <img src={preview} alt="preview" />
        ) : (
          <div className="empty-preview">Media preview appears here</div>
        )}
        <h2>{post.title || 'Post title'}</h2>
        <p>{post.caption || 'Caption preview...'}</p>
        <small>{post.location || 'Location'}</small>
      </div>
    </section>
  )
}
