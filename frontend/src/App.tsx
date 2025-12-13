import { useEffect, useState } from 'react';
import apiClient from './api/axiosClient';

function App() {
  const [message, setMessage] = useState('Loading...');

  useEffect(() => {
    apiClient.get('/health')
      .then(res => {
        // Safety check: ensure we are extracting the string
        if (res.data && typeof res.data.status === 'string') {
            setMessage(res.data.status);
        } else {
            // If the data is weird, print it so we can see it without crashing
            setMessage(JSON.stringify(res.data));
        }
      })
      .catch((err) => {
        console.error(err);
        setMessage('Backend connection failed 😢');
      });
  }, []);

  return (
    <div className="flex min-h-screen items-center justify-center bg-gray-100">
      <div className="p-10 bg-white shadow-xl rounded-xl border border-gray-200">
        <h1 className="text-4xl font-bold text-blue-600 mb-4">Sweet Shop Kata</h1>
        <p className="text-lg text-gray-700 font-semibold">
          System Status: <span className={message.includes('Active') ? "text-green-500" : "text-red-500"}>{message}</span>
        </p>
      </div>
    </div>
  );
}

export default App;