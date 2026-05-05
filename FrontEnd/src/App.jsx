import { Link, NavLink, Route, Routes, useLocation } from 'react-router-dom'
import { Camera, Home, LogIn, Search, User, UploadCloud, BarChart3, LogOut } from 'lucide-react'
import { getUser, logout } from './api'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import FeedPage from './pages/FeedPage'
import CreatorDashboard from './pages/CreatorDashboard'
import SearchPage from './pages/SearchPage'
import ProfilePage from './pages/ProfilePage'
import CreatorAnalyticsPage from './pages/CreatorAnalyticsPage'
import PostDetailPage from './pages/PostDetailPage'

function Layout() {
  const user = getUser()
  const location = useLocation()

  const navClass = ({ isActive }) => isActive ? 'nav-link active' : 'nav-link'

  return (
    <>
      <header className="topbar">
        <Link to="/" className="brand">
          <span className="brand-icon"><Camera size={22} /></span>
          <span>AI Media</span>
        </Link>

        <nav className="desktop-nav">
          <NavLink className={navClass} to="/"><Home size={17} />Feed</NavLink>
          <NavLink className={navClass} to="/search"><Search size={17} />Search</NavLink>
          <NavLink className={navClass} to="/profile"><User size={17} />Profile</NavLink>
          {user.role === 'CREATOR' && (
            <>
              <NavLink className={navClass} to="/creator"><UploadCloud size={17} />Upload</NavLink>
              <NavLink className={navClass} to="/analytics"><BarChart3 size={17} />Analytics</NavLink>
            </>
          )}
        </nav>

        <div className="top-actions">
          {user?.username ? (
            <>
              <span className="role-pill">{user.role}</span>
              <button className="ghost-btn" onClick={logout}><LogOut size={16} />Logout</button>
            </>
          ) : (
            <>
              <Link className="ghost-btn" to="/login"><LogIn size={16} />Login</Link>
              <Link className="primary-btn small" to="/register">Join</Link>
            </>
          )}
        </div>
      </header>

      {location.pathname === '/' && !user?.username && (
        <section className="hero">
          <div>
            <p className="eyebrow">Scalable Advanced Software Solution</p>
            <h1>Creator and consumer media platform with AI features.</h1>
            <p className="hero-text">
              Upload media, generate AI captions, search posts, rate content, comment, and view creator analytics.
            </p>
          </div>
          <Link to={user.role === 'CREATOR' ? '/creator' : '/register'} className="primary-btn">
            {user.role === 'CREATOR' ? 'Create Post' : 'Start Now'}
          </Link>
        </section>
      )}

      <main className="container">
        <Routes>
          <Route path="/" element={<FeedPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/creator" element={<CreatorDashboard />} />
          <Route path="/search" element={<SearchPage />} />
          <Route path="/profile" element={<ProfilePage />} />
          <Route path="/analytics" element={<CreatorAnalyticsPage />} />
          <Route path="/posts/:postId" element={<PostDetailPage />} />
        </Routes>
      </main>
    </>
  )
}

export default function App() {
  return <Layout />
}
