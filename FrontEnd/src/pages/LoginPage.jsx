import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import API from '../api'

export default function LoginPage() {
  const navigate = useNavigate()
  const [form, setForm] = useState({ email: '', password: '' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const submit = async (e) => {
    e.preventDefault()
    setLoading(true)
    setError('')
    try {
      const res = await API.post('/api/auth/login', form)
      const data = res.data.data
      localStorage.setItem('token', data.token)
      localStorage.setItem('user', JSON.stringify(data))
      navigate('/')
    } catch (err) {
      setError(err.response?.data?.message || 'Login failed. Check backend is running.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <section className="auth-page">
      <form className="auth-card" onSubmit={submit}>
        <p className="eyebrow">Welcome back</p>
        <h1>Login to AI Media</h1>
        {error && <div className="error-box">{error}</div>}
        <label>Email</label>
        <input value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} placeholder="creator1@test.com" />
        <label>Password</label>
        <input value={form.password} type="password" onChange={(e) => setForm({ ...form, password: e.target.value })} placeholder="123456" />
        <button className="primary-btn full" disabled={loading}>{loading ? 'Logging in...' : 'Login'}</button>
        <p className="muted">No account? <Link to="/register">Create one</Link></p>
      </form>
    </section>
  )
}
