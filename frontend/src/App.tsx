import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Register from './Register';
import Login from './Login';

function Dashboard() {
  return (
    <div className="p-10 text-center">
      <h1 className="text-3xl font-bold text-gray-800">Welcome to Sweet Shop! 🍬</h1>
      <p className="mt-4 text-gray-600">You are logged in.</p>
    </div>
  );
}

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/" element={<Dashboard />} />
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;