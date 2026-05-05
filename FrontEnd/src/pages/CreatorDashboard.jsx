import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Sparkles, UploadCloud } from 'lucide-react'
import API, { getUser } from '../api'

export default function CreatorDashboard() {
  const navigate = useNavigate()
  const user = getUser()
  const [file, setFile] = useState(null)
  const [preview, setPreview] = useState('')
  const [post, setPost] = useState({ title: '', caption: '', location: '', peopleTags: '' })
  const [loading, setLoading] = useState(false)
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
      const res = await API.post('/api/ai/caption', {
        title: post.title,
        location: post.location,
        peopleTags: post.peopleTags
      })
      setPost({ ...post, caption: `${res.data.caption} ${res.data.hashtags.join(' ')}` })
    } catch {
      setMessage('AI service not available. Start ai-service on port 8086.')
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

        <button type="button" className="secondary-btn" onClick={suggestCaption}><Sparkles size={17} />AI Suggest Caption</button>

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
