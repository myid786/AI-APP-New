import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import API from '../api'

export default function RegisterPage() {
  const navigate = useNavigate()
  const [form, setForm] = useState({
    username: '',
    email: '',
    password: '',
    role: 'CONSUMER'
  })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const submit = async (e) => {
    e.preventDefault()
    setLoading(true)
    setError('')

    try {
      const payload = {
        username: form.username.trim(),
        email: form.email.trim(),
        password: form.password,
        role: form.role
      }

      const res = await API.post('/api/auth/register', payload)
      const user = res.data.data

      localStorage.setItem('token', user.token)
      localStorage.setItem('user', JSON.stringify(user))

      try {
        await API.post('/api/users/profile', {
          userId: user.userId,
          username: user.username,
          role: user.role,
          fullName: '',
          bio: '',
          profileImageUrl: '',
          location: '',
          website: ''
        })
      } catch (profileErr) {
        console.log('Profile creation skipped:', profileErr)
      }

      navigate(user.role === 'CREATOR' ? '/creator' : '/')
    } catch (err) {
      setError(
        err.response?.data?.message ||
        err.response?.data?.data ||
        'Registration failed. Check backend is running.'
      )
    } finally {
      setLoading(false)
    }
  }

  return (
    <section className="auth-page">
      <form className="auth-card" onSubmit={submit}>
        <p className="eyebrow">Create account</p>
        <h1>Join as Creator or Consumer</h1>

        {error && <div className="error-box">{error}</div>}

        <label>Username</label>
        <input
          value={form.username}
          onChange={(e) => setForm({ ...form, username: e.target.value })}
          placeholder="creator1"
          required
        />

        <label>Email</label>
        <input
          value={form.email}
          onChange={(e) => setForm({ ...form, email: e.target.value })}
          placeholder="creator1@test.com"
          required
        />

        <label>Password</label>
        <input
          value={form.password}
          type="password"
          onChange={(e) => setForm({ ...form, password: e.target.value })}
          placeholder="123456"
          required
        />

        <label>Account Type</label>
        <div className="role-choice">
          <button
            type="button"
            className={form.role === 'CONSUMER' ? 'selected' : ''}
            onClick={() => setForm({ ...form, role: 'CONSUMER' })}
          >
            Consumer
          </button>

          <button
            type="button"
            className={form.role === 'CREATOR' ? 'selected' : ''}
            onClick={() => setForm({ ...form, role: 'CREATOR' })}
          >
            Creator
          </button>
        </div>

        <button className="primary-btn full" disabled={loading}>
          {loading ? 'Creating...' : 'Create Account'}
        </button>

        <p className="muted">
          Already registered? <Link to="/login">Login</Link>
        </p>
      </form>
    </section>
  )
}