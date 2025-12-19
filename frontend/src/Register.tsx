import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom'; // <--- 1. Import added
import apiClient from './api/axiosClient';

export default function Register() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [status, setStatus] = useState<'idle' | 'success' | 'error'>('idle');
  
  const navigate = useNavigate(); // <--- 2. Hook initialized

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setStatus('idle');

    try {
      await apiClient.post('/auth/register', { email, password });
      setStatus('success');
      setEmail('');
      setPassword('');

      // <--- 3. Redirect logic added here
      setTimeout(() => {
        navigate('/login');
      }, 1500);

    } catch (error) {
      console.error(error);
      setStatus('error');
    }
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-gray-50">
      <div className="w-full max-w-md p-8 bg-white rounded-lg shadow-md border border-gray-100">
        <h2 className="text-2xl font-bold text-center text-gray-800 mb-6">Join Sweet Shop</h2>
        
        {status === 'success' && (
          <div className="mb-4 p-3 bg-green-100 text-green-700 rounded text-center">
            Registration Successful! Redirecting to login...
          </div>
        )}

        {status === 'error' && (
          <div className="mb-4 p-3 bg-red-100 text-red-700 rounded text-center">
            Registration failed. Please try again.
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700">Email Address</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
              placeholder="you@example.com"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700">Password</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              className="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
              placeholder="••••••••"
            />
          </div>

          <button
            type="submit"
            className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
          >
            Create Account
          </button>
        </form>

        {/* <--- 4. Link section added here */}
        <div className="mt-4 text-center">
          <p className="text-sm text-gray-600">
            Already have an account?{' '}
            <Link to="/login" className="text-blue-600 hover:underline">
              Log in here
            </Link>
          </p>
        </div>

      </div>
    </div>
  );
}