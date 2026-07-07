import { useState } from 'react';
import { Link, Navigate, useNavigate } from 'react-router-dom';
import { ArrowRight, CheckCircle2, Eye, EyeOff, LockKeyhole, ShieldCheck, UserCog, UserRound, UsersRound } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import FleetScene3D from '../../components/FleetScene3D';

const roleProfiles = [
  {
    key: 'admin',
    label: 'Admin',
    role: 'ROLE_ADMIN',
    icon: ShieldCheck,
    email: 'admin@fleet.local',
    password: 'Admin@12345',
    summary: 'Complete control, employees, attendance, salary, analytics and reports.',
  },
  {
    key: 'manager',
    label: 'Manager',
    role: 'ROLE_FLEET_MANAGER',
    icon: UserCog,
    email: 'manager@fleet.local',
    password: '',
    summary: 'Assign tasks, track team, approve leave, attendance and daily reports.',
  },
  {
    key: 'employee',
    label: 'Field Employee',
    role: 'ROLE_DISPATCHER',
    icon: UserRound,
    email: 'employee@fleet.local',
    password: '',
    summary: 'Attendance, live location, task completion, visits and expenses.',
  },
  {
    key: 'hr',
    label: 'HR',
    role: 'ROLE_ADMIN',
    icon: UsersRound,
    email: 'hr@fleet.local',
    password: '',
    summary: 'Employee records, salary, leaves, documents and attendance reports.',
  },
];

export default function LoginPage() {
  const auth = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: 'admin@fleet.local', password: 'Admin@12345' });
  const [selectedRole, setSelectedRole] = useState(roleProfiles[0]);
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const [busy, setBusy] = useState(false);

  if (auth.user) return <Navigate to="/" replace/>;

  function chooseRole(profile) {
    setSelectedRole(profile);
    setError('');
    setForm((current) => ({
      email: profile.email || current.email,
      password: profile.password || current.password,
    }));
  }

  async function submit(e) {
    e.preventDefault();
    setBusy(true);
    setError('');
    try {
      await auth.login(form);
      navigate('/');
    } catch (err) {
      setError(err.message);
    } finally {
      setBusy(false);
    }
  }

  return (
    <main className="authPage">
      <section className="authShell" aria-label="Field Management sign in">
        <div className="authShowcase">
          <FleetScene3D variant="login" />
          <div className="authBrand">
            <div className="brandMark">FMS</div>
            <div>
              <h1>Field Management</h1>
              <p>Secure operations command center</p>
            </div>
          </div>

          <div className="authIntro">
            <p className="eyebrow">Role-based access</p>
            <h2>Control every field workflow from one protected console.</h2>
            <p>Manage employees, attendance, tasks, live GPS, expenses, leave, salary, documents, reports and user permissions with a clean React admin panel.</p>
          </div>

          <div className="authHighlights">
            <span><CheckCircle2 size={17}/> JWT secured session</span>
            <span><CheckCircle2 size={17}/> Admin approval ready</span>
            <span><CheckCircle2 size={17}/> React admin panel</span>
          </div>
        </div>

        <form className="authCard" onSubmit={submit}>
          <div className="authCardHeader">
            <span className="authBadge"><LockKeyhole size={16}/> Secure login</span>
            <h2>Welcome back</h2>
            <p>Choose your access profile and sign in to continue.</p>
          </div>

          <div className="roleGrid" aria-label="Select login role">
            {roleProfiles.map((profile) => {
              const Icon = profile.icon;
              const active = selectedRole.key === profile.key;
              return (
                <button
                  className={`roleOption ${active ? 'active' : ''}`}
                  key={profile.key}
                  type="button"
                  onClick={() => chooseRole(profile)}
                  aria-pressed={active}
                >
                  <Icon size={18}/>
                  <span>{profile.label}</span>
                </button>
              );
            })}
          </div>

          <div className="roleSummary">
            <strong>{selectedRole.role}</strong>
            <span>{selectedRole.summary}</span>
          </div>

          {error && <div className="notice error">{error}</div>}

          <label>
            Email address
            <input
              type="email"
              autoComplete="email"
              value={form.email}
              onChange={(e) => setForm({ ...form, email: e.target.value })}
              required
            />
          </label>

          <label>
            Password
            <span className="passwordField">
              <input
                type={showPassword ? 'text' : 'password'}
                autoComplete="current-password"
                value={form.password}
                onChange={(e) => setForm({ ...form, password: e.target.value })}
                required
              />
              <button
                type="button"
                className="iconButton"
                onClick={() => setShowPassword((visible) => !visible)}
                aria-label={showPassword ? 'Hide password' : 'Show password'}
                title={showPassword ? 'Hide password' : 'Show password'}
              >
                {showPassword ? <EyeOff size={18}/> : <Eye size={18}/>}
              </button>
            </span>
          </label>

          <button className="primary authSubmit" disabled={busy}>
            {busy ? 'Signing in...' : 'Sign in'}
            {!busy && <ArrowRight size={18}/>}
          </button>

          <div className="authFooter">
            <span>New customer?</span>
            <Link to="/register">Create account</Link>
          </div>
        </form>
      </section>
    </main>
  );
}
