import { useEffect, useState } from 'react'
import API, { getUser } from '../api'

export default function ProfilePage() {
  const user = getUser()
  const [profile, setProfile] = useState({ fullName: '', bio: '', location: '', website: '', profileImageUrl: '' })
  const [msg, setMsg] = useState('')

  useEffect(() => {
    if (user.userId) {
      API.get(`/api/users/profile/${user.userId}`)
        .then((res) => setProfile(res.data))
        .catch(() => {})
    }
  }, [user.userId])

  const save = async () => {
    setMsg('')
    try {
      await API.post('/api/users/profile', {
        userId: user.userId,
        username: user.username,
        role: user.role,
        ...profile
      })
      setMsg('Profile saved.')
    } catch {
      await API.put(`/api/users/profile/${user.userId}`, profile)
      setMsg('Profile updated.')
    }
  }

  if (!user?.userId) return <div className="state-card">Please login first.</div>

  return (
    <section className="profile-layout">
      <div className="profile-card">
        <div className="big-avatar">
          {profile.profileImageUrl ? <img src={profile.profileImageUrl} alt="profile" /> : user.username?.charAt(0)}
        </div>
        <h1>{profile.fullName || user.username}</h1>
        <p>{profile.bio || 'Add your bio to make profile professional.'}</p>
        <span className="role-pill">{user.role}</span>
      </div>

      <div className="form-panel">
        <h2>Edit Profile</h2>
        {msg && <div className="success-box">{msg}</div>}
        <label>Full Name</label>
        <input value={profile.fullName || ''} onChange={(e) => setProfile({ ...profile, fullName: e.target.value })} />
        <label>Profile Image URL</label>
        <input value={profile.profileImageUrl || ''} onChange={(e) => setProfile({ ...profile, profileImageUrl: e.target.value })} />
        <label>Bio</label>
        <textarea value={profile.bio || ''} onChange={(e) => setProfile({ ...profile, bio: e.target.value })} />
        <label>Location</label>
        <input value={profile.location || ''} onChange={(e) => setProfile({ ...profile, location: e.target.value })} />
        <label>Website</label>
        <input value={profile.website || ''} onChange={(e) => setProfile({ ...profile, website: e.target.value })} />
        <button className="primary-btn" onClick={save}>Save Profile</button>
      </div>
    </section>
  )
}
