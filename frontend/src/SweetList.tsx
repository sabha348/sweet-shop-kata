import { useEffect, useState } from 'react';
import apiClient from './api/axiosClient';

interface Sweet {
  id: number;
  name: string;
  price: number;
  imageUrl: string;
}

export default function SweetList() {
  const [sweets, setSweets] = useState<Sweet[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    apiClient.get('/sweets')
      .then(response => {
        setSweets(response.data);
        setLoading(false);
      })
      .catch(error => {
        console.error("Error fetching sweets:", error);
        setLoading(false);
      });
  }, []);

  if (loading) return <div className="text-center p-10">Loading yummy sweets...</div>;

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 p-6">
      {sweets.map(sweet => (
        <div key={sweet.id} className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow">
          <img 
            src={sweet.imageUrl} 
            alt={sweet.name} 
            className="w-full h-48 object-cover"
          />
          <div className="p-4">
            <h3 className="text-xl font-semibold text-gray-800">{sweet.name}</h3>
            <p className="text-gray-600 mt-2">₹{sweet.price}</p>
            <button className="mt-4 w-full bg-blue-600 text-white py-2 px-4 rounded hover:bg-blue-700">
              Add to Cart
            </button>
          </div>
        </div>
      ))}
    </div>
  );
}