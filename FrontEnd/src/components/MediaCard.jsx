import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { MapPin, MessageCircle, ThumbsDown, ThumbsUp, UserRound } from 'lucide-react'
import API from '../api'

export default function MediaCard({ post }) {
  const [likes, setLikes] = useState(0)
  const [dislikes, setDislikes] = useState(0)
  const [comments, setComments] = useState([])
  const [text, setText] = useState('')
  const [showLoginPopup, setShowLoginPopup] = useState(false)

  const user = JSON.parse(localStorage.getItem('user') || '{}')
  const isLoggedIn = !!user?.userId

  useEffect(() => {
    API.get(`/api/interactions/comments/${post.id}`)
      .then((res) => setComments(res.data))
      .catch(() => {})
  }, [post.id])

  const requireLogin = () => {
    if (!isLoggedIn) {
      setShowLoginPopup(true)
      return false
    }
    return true
  }

  const handleLike = () => {
    if (!requireLogin()) return
    setLikes((prev) => prev + 1)
  }

  const handleDislike = () => {
    if (!requireLogin()) return
    setDislikes((prev) => prev + 1)
  }

  const addComment = async () => {
    if (!requireLogin()) return
    if (!text.trim()) return

    const res = await API.post('/api/interactions/comments', {
      postId: post.id,
      userId: user.userId,
      username: user.username,
      text: text.trim()
    })

    setComments([...comments, res.data])
    setText('')
  }

  return (
    <article className="media-card">
      <div className="card-head">
        <div className="avatar">
          {post.creatorUsername?.charAt(0)?.toUpperCase() || 'C'}
        </div>

        <div>
          <h3>{post.creatorUsername || 'Creator'}</h3>
          <p>
            <MapPin size={14} /> {post.location || 'No location'}
          </p>
        </div>
      </div>

      <Link to={`/posts/${post.id}`} className="media-wrap">
        {post.mediaType === 'video' ? (
          <video src={post.mediaUrl} controls />
        ) : (
          <img
            src={
              post.mediaUrl ||
              'https://images.unsplash.com/photo-1500530855697-b586d89ba3ee'
            }
            alt={post.title}
          />
        )}
      </Link>

      <div className="card-body">
        <h2>{post.title}</h2>
        <p>{post.caption}</p>

        <div className="tag-row">
          {(post.peopleTags || '')
            .split(',')
            .filter(Boolean)
            .slice(0, 4)
            .map((tag) => (
              <span key={tag}>
                <UserRound size={13} />
                {tag.trim()}
              </span>
            ))}
        </div>
      </div>

      <div className="card-actions">
        <button type="button" onClick={handleLike}>
          <ThumbsUp size={17} /> Like {likes}
        </button>

        <button type="button" onClick={handleDislike}>
          <ThumbsDown size={17} /> Dislike {dislikes}
        </button>

        <button type="button" onClick={requireLogin}>
          <MessageCircle size={17} /> Comment
        </button>
      </div>

      <div style={{ padding: '0 16px 16px' }}>
        <div style={{ display: 'flex', gap: '8px' }}>
          <input
            value={text}
            onChange={(e) => setText(e.target.value)}
            onFocus={requireLogin}
            placeholder="Add a comment..."
          />

          <button type="button" onClick={addComment}>
            Post
          </button>
        </div>

        <div style={{ marginTop: '12px' }}>
          {comments.slice(0, 3).map((c) => (
            <p key={c.id} style={{ margin: '8px 0' }}>
              <b>{c.username}</b>: {c.text}
            </p>
          ))}
        </div>
      </div>

      {showLoginPopup && (
        <div className="login-popup-overlay">
          <div className="login-popup">
            <h2>Login required</h2>
            <p>Please login or register to like, dislike, or comment.</p>

            <div className="popup-actions">
              <Link to="/login" className="primary-btn">
                Login
              </Link>

              <Link to="/register" className="secondary-btn">
                Register
              </Link>

              <button
                type="button"
                className="ghost-btn"
                onClick={() => setShowLoginPopup(false)}
              >
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}
    </article>
  )
}