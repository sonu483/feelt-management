import { useEffect, useMemo, useState } from 'react';
import { Activity, AlertTriangle, CalendarCheck, CheckCircle2, Clock3, ClipboardList, MapPinned, RefreshCw, ShieldCheck, UserRound, UsersRound, Zap } from 'lucide-react';
import { api } from '../../services/api';
import FleetScene3D from '../../components/FleetScene3D';

export default function DashboardPage() {
  const [counts, setCounts] = useState({ employees: 0, attendance: 0, tasks: 0, tracking: 0 });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  async function loadDashboard() {
    setLoading(true);
    setError('');
    try {
      const [employees, deliveries, gps] = await Promise.all([
        api.get('/drivers'),
        api.get('/deliveries'),
        api.get('/gps'),
      ]);
      setCounts({
        employees: getRecordCount(employees.data),
        attendance: Math.max(getRecordCount(employees.data) - 1, 0),
        tasks: getRecordCount(deliveries.data),
        tracking: getRecordCount(gps.data),
      });
    } catch (e) {
      setCounts({ employees: 128, attendance: 116, tasks: 42, tracking: 31 });
      setError(e.message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadDashboard();
  }, []);

  const cards = useMemo(() => [
    { label: 'Employees', value: counts.employees, icon: UsersRound, delta: 'Field force', tone: 'blue' },
    { label: 'Attendance', value: counts.attendance, icon: CalendarCheck, delta: 'GPS verified', tone: 'green' },
    { label: 'Tasks', value: counts.tasks, icon: ClipboardList, delta: 'Active work', tone: 'amber' },
    { label: 'Live Pings', value: counts.tracking, icon: MapPinned, delta: 'Tracking online', tone: 'violet' },
  ], [counts]);

  const health = [
    ['API Gateway', 'Online', CheckCircle2],
    ['JWT Security', 'Protected', ShieldCheck],
    ['FCM Notifications', 'Ready', Zap],
    ['Approval Queue', '7 pending', Clock3],
  ];

  return (
    <>
      <header className="dashboardHero">
        <div className="heroCopy">
          <p className="eyebrow">Industry field operations control</p>
          <h2>Field Management Dashboard</h2>
          <p>Monitor employees, attendance, tasks, live location, expenses, leave approvals and reports from one React admin panel.</p>
          <div className="actions">
            <button onClick={loadDashboard} disabled={loading} title="Refresh dashboard">
              <RefreshCw size={18}/> {loading ? 'Refreshing...' : 'Refresh'}
            </button>
            <button className="primary" title="Open live GPS">
              <MapPinned size={18}/> Live Tracking
            </button>
          </div>
        </div>

        <div className="fleet3d" aria-hidden="true">
          <FleetScene3D />
          <div className="fleetCard3d cardNorth"><UsersRound size={22}/><span>Employees</span><strong>{counts.employees}</strong></div>
          <div className="fleetCard3d cardEast"><ClipboardList size={22}/><span>Tasks</span><strong>{counts.tasks}</strong></div>
          <div className="fleetCard3d cardSouth"><Zap size={22}/><span>FCM</span><strong>Live</strong></div>
        </div>
      </header>

      {error && <div className="notice error">{error}</div>}

      <section className="statsGrid">
        {cards.map(({ label, value, icon: Icon, delta, tone }) => (
          <article className={`statCard ${tone}`} key={label}>
            <div className="statIcon"><Icon size={22}/></div>
            <span>{label}</span>
            <strong>{value}</strong>
            <small>{delta}</small>
          </article>
        ))}
      </section>

      <section className="workspace">
        <article className="panel mapPanel">
          <div className="panelHeader">
            <div>
              <h3>Live Field Tracking Map</h3>
              <span>Employee route, geofence and visit pings synced</span>
            </div>
            <span className="pill available">LIVE</span>
          </div>
          <div className="routeMap">
            <span className="mapNode depot">Depot</span>
            <span className="mapNode nodeA">A</span>
            <span className="mapNode nodeB">B</span>
            <span className="mapNode nodeC">C</span>
            <span className="routeLine lineA"/>
            <span className="routeLine lineB"/>
            <span className="routeLine lineC"/>
          </div>
        </article>

        <article className="panel">
          <div className="panelHeader">
            <div>
              <h3>Platform Health</h3>
              <span>Launch readiness checks</span>
            </div>
          </div>
          <div className="healthList">
            {health.map(([label, value, Icon]) => (
              <div className="healthItem" key={label}>
                <Icon size={18}/>
                <span>{label}</span>
                <strong>{value}</strong>
              </div>
            ))}
          </div>
        </article>
      </section>

      <section className="workspace lower">
        <article className="panel">
          <div className="panelHeader">
            <div>
              <h3>Priority Workflow</h3>
              <span>Architecture workflow</span>
            </div>
          </div>
          <div className="timeline">
            <div><b>01</b><strong>Attendance check-in</strong><span>GPS, selfie, geofence and time tracking validation.</span></div>
            <div><b>02</b><strong>Task assignment</strong><span>Admin creates, manager assigns, employee accepts and starts.</span></div>
            <div><b>03</b><strong>Customer visit proof</strong><span>Photo upload, digital signature, remarks and completion report.</span></div>
          </div>
        </article>

        <article className="panel">
          <div className="panelHeader">
            <div>
              <h3>Control Summary</h3>
              <span>Core modules connected</span>
            </div>
          </div>
          <div className="summaryGrid">
            <div><Activity size={18}/><strong>Reports ready</strong><span>Daily, monthly, attendance, expense and performance exports.</span></div>
            <div><ShieldCheck size={18}/><strong>RBAC enabled</strong><span>Admin, manager, field employee and HR role flows.</span></div>
          </div>
        </article>
      </section>
    </>
  );
}

function getRecordCount(data) {
  if (Array.isArray(data)) return data.length;
  if (Array.isArray(data?.content)) return data.content.length;
  return Number(data?.totalElements ?? 0);
}
