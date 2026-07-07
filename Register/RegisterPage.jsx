import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

export default function RegisterPage() {
  const auth=useAuth(); const navigate=useNavigate(); const [form,setForm]=useState({fullName:'',email:'',password:''}); const [error,setError]=useState('');
  async function submit(e){e.preventDefault();setError('');try{await auth.register(form);navigate('/');}catch(err){setError(err.message)}}
  return <div className="authPage"><form className="authCard" onSubmit={submit}><div className="brandMark">FMS</div><h1>Create account</h1>{error&&<div className="notice error">{error}</div>}<label>Full name<input value={form.fullName} onChange={e=>setForm({...form,fullName:e.target.value})} required/></label><label>Email<input type="email" value={form.email} onChange={e=>setForm({...form,email:e.target.value})} required/></label><label>Password<input type="password" minLength="8" value={form.password} onChange={e=>setForm({...form,password:e.target.value})} required/></label><button className="primary">Register</button><Link to="/login">Back to login</Link></form></div>;
}
