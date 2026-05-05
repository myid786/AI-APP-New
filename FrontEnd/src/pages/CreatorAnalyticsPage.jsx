import { useEffect, useState } from 'react'
import API, { getUser } from '../api'

export default function CreatorAnalyticsPage() {
  const user = getUser()
  const [items, setItems] = useState([])
  const [status, setStatus] = useState('loading')

  useEffect(() => {
    if (!user.userId) {
      setStatus('noauth')
      return
    }

    API.get(`/api/posts/creator/${user.userId}`)
      .then(async (res) => {
        const data = []
        for (const post of res.data) {
          try {
            const analytics = await API.get(`/api/interactions/analytics/${post.id}`)
            data.push({ post, analytics: analytics.data })
          } catch {
            data.push({ post, analytics: null })
          }
        }
        setItems(data)
        setStatus('done')
      })
      .catch(() => setStatus('error'))
  }, [user.userId])

  if (status === 'noauth') return <div className="state-card">Please login as creator.</div>
  if (status === 'loading') return <div className="state-card">Loading analytics...</div>
  if (status === 'error') return <div className="state-card error-box">Could not load analytics.</div>

  return (
    <section>
      <div className="section-head">
        <div>
          <p className="eyebrow">Creator Analytics</p>
          <h1>Post performance and sentiment</h1>
        </div>
      </div>

      {items.length === 0 ? <div className="state-card">No creator posts yet.</div> : (
        <div className="analytics-grid">
          {items.map(({ post, analytics }) => (
            <div className="analytics-card" key={post.id}>
              <h2>{post.title}</h2>
              <p>{post.caption}</p>
              {analytics ? (
                <div className="metric-grid">
                  <div><strong>{analytics.totalComments}</strong><span>Comments</span></div>
                  <div><strong>{analytics.averageRating.toFixed(1)}</strong><span>Avg Rating</span></div>
                  <div><strong>{analytics.positiveComments}</strong><span>Positive</span></div>
                  <div><strong>{analytics.negativeComments}</strong><span>Negative</span></div>
                </div>
              ) : <p className="muted">No analytics yet.</p>}
            </div>
          ))}
        </div>
      )}
    </section>
  )
}
