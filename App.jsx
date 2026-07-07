import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import AppLayout from './components/Layout/AppLayout';
import ProtectedRoute from './components/ProtectedRoute';
import LoginPage from './pages/Login/LoginPage';
import RegisterPage from './pages/Register/RegisterPage';
import DashboardPage from './pages/Dashboard/DashboardPage';
import ModulePage from './pages/ModulePage';

const modules = {
  employees: 'Employee Management',
  attendance: 'Attendance',
  tasks: 'Task Management',
  tracking: 'Live Tracking',
  visits: 'Customer Visits',
  expenses: 'Expense Management',
  leaves: 'Leave Management',
  notifications: 'Notifications',
  chat: 'Team Chat',
  reports: 'Reports & Analytics',
  salary: 'Salary & Payslips',
  complaints: 'Complaints',
  assets: 'Asset Management',
  inventory: 'Inventory',
  visitors: 'Visitor Management',
  documents: 'Documents',
  users: 'User Management',
  profile: 'Profile',
  settings: 'System Settings',
};

export default function App(){return <BrowserRouter><AuthProvider><Routes><Route path="/login" element={<LoginPage/>}/><Route path="/register" element={<RegisterPage/>}/><Route element={<ProtectedRoute/>}><Route element={<AppLayout/>}><Route index element={<DashboardPage/>}/>{Object.entries(modules).map(([path,title])=><Route key={path} path={path} element={<ModulePage moduleKey={path} title={title} description={`Manage ${title.toLowerCase()} with role-based access and operational controls.`}/>}/>)}</Route></Route></Routes></AuthProvider></BrowserRouter>}
