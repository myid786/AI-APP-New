import { useEffect, useState } from 'react'
import API from '../api'
import MediaCard from '../components/MediaCard'

export default function FeedPage() {
  const [posts, setPosts] = useState([])
  const [status, setStatus] = useState('loading')

  useEffect(() => {
    API.get('/api/posts')
      .then((res) => {
        setPosts(res.data)
        setStatus('done')
      })
      .catch(() => setStatus('error'))
  }, [])

  if (status === 'loading') return <div className="state-card">Loading feed...</div>
  if (status === 'error') return <div className="state-card error-box">Could not load feed. Start backend gateway on port 8080.</div>

  return (
    <section>

      {/* <div className="feed-container">
  {posts.map(post => (
    <MediaCard key={post.id} post={post} />
  ))}
</div> */}
      <div className="section-head">
        <div>
          <p className="eyebrow">Consumer View</p>
          <h2>Latest Media Feed</h2>
        </div>
      </div>
      {posts.length === 0 ? (
        <div className="state-card">No posts yet. Login as creator and upload your first media.</div>
      ) : (
        <div className="grid-feed">
          {posts.map((post) => <MediaCard key={post.id} post={post} />)}
        </div>
      )}
    </section>
  )
}
