import { useState } from 'react'
import API from '../api'
import MediaCard from '../components/MediaCard'

export default function SearchPage() {
  const [q, setQ] = useState('')
  const [results, setResults] = useState([])
  const [searched, setSearched] = useState(false)

  const search = async (e) => {
    e.preventDefault()
    setSearched(true)
    const res = await API.get(`/api/posts/search?q=${encodeURIComponent(q)}`)
    setResults(res.data)
  }

  return (
    <section>
      <form className="search-card" onSubmit={search}>
        <p className="eyebrow">Discover Content</p>
        <h1>Search by title, caption or location</h1>
        <div className="search-row">
          <input value={q} onChange={(e) => setQ(e.target.value)} placeholder="Try Manchester, travel, food..." />
          <button className="primary-btn">Search</button>
        </div>
      </form>

      {searched && results.length === 0 && <div className="state-card">No matching posts found.</div>}

      <div className="grid-feed">
        {results.map((post) => <MediaCard key={post.id} post={post} />)}
      </div>
    </section>
  )
}
