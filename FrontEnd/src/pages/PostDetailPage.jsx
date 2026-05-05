import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import API, { getUser } from '../api'

export default function PostDetailPage() {
  const { postId } = useParams()
  const user = getUser()
  const [post, setPost] = useState(null)
  const [comments, setComments] = useState([])
  const [text, setText] = useState('')
  const [rating, setRating] = useState(5)

  const load = async () => {
    const posts = await API.get('/api/posts')
    setPost(posts.data.find((p) => String(p.id) === String(postId)))
    const c = await API.get(`/api/interactions/comments/${postId}`)
    setComments(c.data)
  }

  useEffect(() => { load() }, [postId])

  const comment = async () => {
    await API.post('/api/interactions/comments', {
      postId: Number(postId),
      userId: user.userId,
      username: user.username,
      text
    })
    setText('')
    load()
  }

  const rate = async () => {
    await API.post('/api/interactions/ratings', {
      postId: Number(postId),
      userId: user.userId,
      rating: Number(rating)
    })
    alert('Rating saved')
  }

  if (!post) return <div className="state-card">Loading post...</div>

  return (
    <section className="detail-layout">
      <div className="detail-media">
        {post.mediaType === 'video' ? <video src={post.mediaUrl} controls /> : <img src={post.mediaUrl} alt={post.title} />}
      </div>
      <div className="detail-panel">
        <p className="eyebrow">{post.creatorUsername}</p>
        <h1>{post.title}</h1>
        <p>{post.caption}</p>
        <small>{post.location}</small>

        <div className="rating-box">
          <select value={rating} onChange={(e) => setRating(e.target.value)}>
            {[1,2,3,4,5].map((n) => <option key={n} value={n}>{n} Star</option>)}
          </select>
          <button className="primary-btn small" onClick={rate}>Rate</button>
        </div>

        <div className="comment-box">
          <textarea value={text} onChange={(e) => setText(e.target.value)} placeholder="Write your comment..." />
          <button className="primary-btn" onClick={comment}>Add Comment</button>
        </div>

        <h3>Comments</h3>
        {comments.map((c) => (
          <div className="comment" key={c.id}>
            <strong>{c.username}</strong>
            <span>{c.sentiment}</span>
            <p>{c.text}</p>
          </div>
        ))}
      </div>
    </section>
  )
}
