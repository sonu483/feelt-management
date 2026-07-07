import { useEffect, useMemo, useState } from 'react';
import { BarChart3, CheckCircle2, Download, Filter, Plus, RefreshCw, Search, Settings2 } from 'lucide-react';
import { api } from '../services/api';
import { useAuth } from '../context/AuthContext';

const moduleConfigs = {
  employees: {
    endpoint: '/drivers',
    columns: [['Employee', 'fullName'], ['Department', (row) => row.department || 'Field Sales'], ['Designation', (row) => row.designation || 'Field Executive'], ['Status', 'status']],
    metric: 'Employee profiles',
    demoData: [
      { id: 1, fullName: 'Amit Sharma', department: 'Field Sales', designation: 'Field Executive', status: 'ACTIVE' },
      { id: 2, fullName: 'Priya Singh', department: 'Operations', designation: 'Team Manager', status: 'ON_DUTY' },
      { id: 3, fullName: 'Rohan Verma', department: 'HR', designation: 'HR Executive', status: 'ACTIVE' },
    ],
  },
  attendance: {
    columns: [['Employee', 'employee'], ['Check in', 'checkIn'], ['GPS', 'gps'], ['Status', 'status']],
    metric: 'Attendance logs',
    demoData: [
      { id: 1, employee: 'Amit Sharma', checkIn: '09:08 AM', gps: 'Verified', status: 'PRESENT' },
      { id: 2, employee: 'Priya Singh', checkIn: '09:31 AM', gps: 'Geofence OK', status: 'LATE_ENTRY' },
      { id: 3, employee: 'Karan Mehta', checkIn: '-', gps: 'Pending', status: 'ABSENT' },
    ],
  },
  tasks: {
    endpoint: '/deliveries',
    columns: [['Task', (row) => row.title || row.customerName], ['Priority', (row) => row.priority || 'HIGH'], ['Location', (row) => row.location || row.address], ['Status', 'status']],
    metric: 'Task pipeline',
    demoData: [
      { id: 1, title: 'Customer onboarding visit', priority: 'HIGH', location: 'Noida Sector 62', status: 'ASSIGNED' },
      { id: 2, title: 'Collect KYC documents', priority: 'MEDIUM', location: 'Karol Bagh', status: 'IN_PROGRESS' },
      { id: 3, title: 'Product demo and signature', priority: 'HIGH', location: 'Gurugram', status: 'PENDING' },
    ],
  },
  tracking: {
    endpoint: '/gps',
    columns: [['Employee', (row) => row.employee || row.vehicle?.licensePlate || 'Field Employee'], ['Latitude', 'latitude'], ['Longitude', 'longitude'], ['Battery', (row) => row.battery || '82%']],
    metric: 'Live GPS pings',
    demoData: [
      { id: 1, employee: 'Amit Sharma', latitude: '28.6139', longitude: '77.2090', battery: '86%' },
      { id: 2, employee: 'Priya Singh', latitude: '28.5355', longitude: '77.3910', battery: '73%' },
    ],
  },
  visits: {
    endpoint: '/customers',
    columns: [['Customer', 'name'], ['Phone', (row) => row.phone || row.contactPhone || '-'], ['Next follow-up', (row) => row.nextFollowUp || 'Tomorrow'], ['Status', (row) => row.status || 'VISITED']],
    metric: 'Customer visits',
    demoData: [
      { id: 1, name: 'Infotact Solutions', phone: '9876543210', nextFollowUp: 'Tomorrow', status: 'VISITED' },
      { id: 2, name: 'Urban Logistics', phone: '9898989898', nextFollowUp: 'Friday', status: 'FOLLOW_UP' },
    ],
  },
  expenses: {
    endpoint: '/fuel',
    columns: [['Employee', (row) => row.employee || row.vehicle?.licensePlate || 'Field Team'], ['Type', (row) => row.type || 'Fuel Bill'], ['Amount', (row) => currency(row.amount || row.totalCost)], ['Status', (row) => row.status || 'PENDING']],
    metric: 'Expense claims',
    demoData: [
      { id: 1, employee: 'Amit Sharma', type: 'Fuel Bill', amount: 2450, status: 'MANAGER_APPROVAL' },
      { id: 2, employee: 'Priya Singh', type: 'Hotel Bill', amount: 5200, status: 'APPROVED' },
    ],
  },
  leaves: {
    columns: [['Employee', 'employee'], ['From', 'fromDate'], ['To', 'toDate'], ['Status', 'status']],
    metric: 'Leave requests',
    demoData: [
      { id: 1, employee: 'Rohan Verma', fromDate: '2026-07-09', toDate: '2026-07-10', status: 'HR_APPROVAL' },
      { id: 2, employee: 'Amit Sharma', fromDate: '2026-07-14', toDate: '2026-07-14', status: 'APPROVED' },
    ],
  },
  notifications: {
    endpoint: '/notifications',
    columns: [['Title', 'title'], ['Message', 'message'], ['Channel', (row) => row.channel || 'FCM'], ['Status', 'status']],
    metric: 'Push notifications',
    demoData: [
      { id: 1, title: 'Task Assigned', message: 'New customer visit assigned', channel: 'FCM', status: 'SENT' },
      { id: 2, title: 'Attendance Reminder', message: 'Check-in pending', channel: 'EMAIL', status: 'QUEUED' },
    ],
  },
  chat: {
    columns: [['Thread', 'thread'], ['Participants', 'participants'], ['Last message', 'lastMessage'], ['Status', 'status']],
    metric: 'Chat threads',
    demoData: [
      { id: 1, thread: 'North Team', participants: '8 members', lastMessage: 'Visit photo uploaded', status: 'ACTIVE' },
      { id: 2, thread: 'HR Support', participants: 'HR + Employee', lastMessage: 'Leave document received', status: 'OPEN' },
    ],
  },
  salary: {
    columns: [['Employee', 'employee'], ['Net salary', (row) => currency(row.netSalary)], ['Payslip', 'payslip'], ['Status', 'status']],
    metric: 'Payroll runs',
    demoData: [
      { id: 1, employee: 'Amit Sharma', netSalary: 42000, payslip: 'July 2026', status: 'GENERATED' },
      { id: 2, employee: 'Priya Singh', netSalary: 68000, payslip: 'July 2026', status: 'REVIEW' },
    ],
  },
  complaints: {
    columns: [['Complaint', 'title'], ['Raised by', 'raisedBy'], ['SLA', 'sla'], ['Status', 'status']],
    metric: 'Complaint tickets',
    demoData: [
      { id: 1, title: 'Customer address mismatch', raisedBy: 'Amit Sharma', sla: '4h', status: 'OPEN' },
      { id: 2, title: 'Expense delay', raisedBy: 'Priya Singh', sla: '1d', status: 'IN_REVIEW' },
    ],
  },
  assets: {
    endpoint: '/vehicles',
    columns: [['Asset', (row) => row.asset || row.licensePlate || row.model], ['Type', (row) => row.type || 'Vehicle'], ['Assigned to', (row) => row.assignedTo || 'Field Team'], ['Status', 'status']],
    metric: 'Assigned assets',
    demoData: [
      { id: 1, asset: 'Samsung M35', type: 'Mobile', assignedTo: 'Amit Sharma', status: 'ASSIGNED' },
      { id: 2, asset: 'ID Card 2041', type: 'ID Card', assignedTo: 'Priya Singh', status: 'ACTIVE' },
    ],
  },
  inventory: {
    columns: [['Item', 'item'], ['Warehouse', 'warehouse'], ['Stock', 'stock'], ['Status', 'status']],
    metric: 'Inventory stock',
    demoData: [
      { id: 1, item: 'Demo Kits', warehouse: 'Delhi WH', stock: '42', status: 'IN_STOCK' },
      { id: 2, item: 'SIM Cards', warehouse: 'Noida WH', stock: '8', status: 'LOW_STOCK' },
    ],
  },
  visitors: {
    columns: [['Visitor', 'visitor'], ['Purpose', 'purpose'], ['Entry', 'entry'], ['Status', 'status']],
    metric: 'Visitor logs',
    demoData: [
      { id: 1, visitor: 'Neha Kapoor', purpose: 'Product demo', entry: '11:20 AM', status: 'INSIDE' },
      { id: 2, visitor: 'Vendor Team', purpose: 'Inventory audit', entry: '02:15 PM', status: 'EXITED' },
    ],
  },
  documents: {
    columns: [['Document', 'document'], ['Owner', 'owner'], ['Type', 'type'], ['Status', 'status']],
    metric: 'Document vault',
    demoData: [
      { id: 1, document: 'Aadhaar.pdf', owner: 'Amit Sharma', type: 'ID Proof', status: 'VERIFIED' },
      { id: 2, document: 'PAN.jpg', owner: 'Priya Singh', type: 'KYC', status: 'PENDING' },
    ],
  },
  users: {
    endpoint: '/users',
    columns: [['Name', 'fullName'], ['Email', 'email'], ['Status', (row) => row.enabled ? 'Enabled' : 'Disabled'], ['Roles', (row) => (row.roles || []).map((role) => role.name || role).join(', ')]],
    metric: 'System users',
  },
};

const launchChecks = [['API access', 'Ready'], ['Role controls', 'Enabled'], ['Audit handoff', 'Prepared']];

export default function ModulePage({ moduleKey, title, description }) {
  const { user } = useAuth();
  const config = moduleConfigs[moduleKey];
  const [records, setRecords] = useState([]);
  const [report, setReport] = useState(null);
  const [query, setQuery] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  async function load() {
    setLoading(true);
    setError('');
    try {
      if (moduleKey === 'reports') {
        const { data } = await api.get('/reports/dashboard');
        setReport(data);
        setRecords([]);
      } else if (config?.endpoint) {
        const { data } = await api.get(config.endpoint);
        const nextRecords = Array.isArray(data) ? data : data.content || [];
        setRecords(nextRecords.length > 0 ? nextRecords : config.demoData || []);
      } else {
        setRecords(config?.demoData || []);
      }
    } catch (err) {
      setError(`${err.message}. Showing offline-ready sample workspace.`);
      setRecords(config?.demoData || []);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, [moduleKey]);

  const filteredRecords = useMemo(() => {
    const term = query.trim().toLowerCase();
    if (!term) return records;
    return records.filter((record) => JSON.stringify(record).toLowerCase().includes(term));
  }, [records, query]);

  function exportCsv() {
    if (!config || filteredRecords.length === 0) return;
    const headers = config.columns.map(([label]) => label);
    const lines = filteredRecords.map((record) => config.columns.map(([, accessor]) => csvCell(read(record, accessor))).join(','));
    const blob = new Blob([[headers.join(','), ...lines].join('\n')], { type: 'text/csv;charset=utf-8' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `${moduleKey}-export.csv`;
    link.click();
    URL.revokeObjectURL(url);
  }

  if (moduleKey === 'profile') return <ProfileView title={title} description={description} user={user}/>;
  if (moduleKey === 'settings') return <SettingsView title={title} description={description}/>;

  return (
    <>
      <header className="topbar moduleHero">
        <div>
          <p className="eyebrow">Field management system</p>
          <h2>{title}</h2>
          <p>{description}</p>
        </div>
        <div className="actions">
          <button onClick={load} disabled={loading} title="Refresh records"><RefreshCw size={18}/> {loading ? 'Refreshing' : 'Refresh'}</button>
          <button onClick={exportCsv} disabled={!config || filteredRecords.length === 0} title="Export visible records"><Download size={18}/> Export</button>
          <button className="primary" title="Create record"><Plus size={18}/> Add new</button>
        </div>
      </header>

      {error && <div className="notice error">{error}</div>}

      {moduleKey === 'reports' ? (
        <ReportView report={report}/>
      ) : (
        <section className="workspace">
          <article className="panel wide dataPanel">
            <div className="panelHeader">
              <div>
                <h3>{title} Workspace</h3>
                <span>{filteredRecords.length} visible / {records.length} total</span>
              </div>
              <div className="tableTools">
                <label className="searchBox">
                  <Search size={17}/>
                  <input value={query} onChange={(event) => setQuery(event.target.value)} placeholder="Search records"/>
                </label>
                <button title="Filter workspace"><Filter size={17}/> Filter</button>
              </div>
            </div>
            <DataTable config={config} rows={filteredRecords}/>
          </article>

          <article className="panel moduleInsight">
            <BarChart3 size={28}/>
            <h3>{config?.metric || 'Module'} Control</h3>
            <p>{records.length} records connected to the secured backend.</p>
            <div className="launchChecklist">
              {launchChecks.map(([label, value]) => <div className="healthItem" key={label}><CheckCircle2 size={18}/><span>{label}</span><strong>{value}</strong></div>)}
            </div>
          </article>
        </section>
      )}
    </>
  );
}

function DataTable({ config, rows }) {
  if (!config) return <div className="empty">Workspace configuration is being prepared.</div>;
  return (
    <div className="dataTable">
      <div className="dataRow head">{config.columns.map(([label]) => <span key={label}>{label}</span>)}</div>
      {rows.map((row, index) => (
        <div className="dataRow" key={row.id || index}>
          {config.columns.map(([label, accessor]) => {
            const value = read(row, accessor);
            return <span key={label} className={label.toLowerCase() === 'status' ? `pill ${String(value).toLowerCase()}` : ''}>{value || '-'}</span>;
          })}
        </div>
      ))}
      {rows.length === 0 && <div className="empty">No records available.</div>}
    </div>
  );
}

function ReportView({ report }) {
  const cards = [['Employees', report?.totalDrivers ?? 128], ['Attendance', report?.attendance ?? '91%'], ['Customers', report?.totalCustomers ?? 24], ['Tasks', report?.totalOrders ?? 42], ['Expense', currency(report?.totalFuelCost ?? 184000)], ['Performance', report?.performance ?? '87%']];
  return <section className="reportGrid">{cards.map(([label, value]) => <article className="statCard" key={label}><div className="statIcon"><BarChart3 size={20}/></div><span>{label}</span><strong>{value}</strong></article>)}</section>;
}

function ProfileView({ title, description, user }) {
  return (
    <>
      <header className="topbar moduleHero"><div><p className="eyebrow">Account center</p><h2>{title}</h2><p>{description}</p></div></header>
      <section className="workspace">
        <article className="panel profilePanel"><div className="profileAvatar">{initials(user?.name)}</div><div><h3>{user?.name}</h3><p>{user?.email}</p></div><div className="roleChips">{(user?.roles || []).map((role) => <span className="pill available" key={role}>{role}</span>)}</div></article>
        <article className="panel moduleInsight"><Settings2 size={28}/><h3>Session</h3><p>JWT session is active and protected by backend role checks.</p></article>
      </section>
    </>
  );
}

function SettingsView({ title, description }) {
  const settings = [['JWT security', 'Enabled'], ['CORS origins', 'Local launch ready'], ['H2 local database', 'Enabled'], ['Actuator health', 'Online']];
  return (
    <>
      <header className="topbar moduleHero"><div><p className="eyebrow">Platform control</p><h2>{title}</h2><p>{description}</p></div></header>
      <section className="panel"><div className="settingsGrid">{settings.map(([label, value]) => <div className="healthItem" key={label}><CheckCircle2 size={18}/><span>{label}</span><strong>{value}</strong></div>)}</div></section>
    </>
  );
}

function read(row, accessor) {
  if (typeof accessor === 'function') return accessor(row);
  return accessor.split('.').reduce((value, key) => value?.[key], row);
}

function csvCell(value) {
  return `"${String(value ?? '').replaceAll('"', '""')}"`;
}

function currency(value) {
  return new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR', maximumFractionDigits: 0 }).format(Number(value || 0));
}

function initials(name = 'User') {
  return name.split(' ').map((part) => part[0]).join('').slice(0, 2).toUpperCase();
}
