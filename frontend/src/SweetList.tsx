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
  const [error, setError] = useState("");

  useEffect(() => {
    // Defined as async function for cleaner syntax
    const fetchSweets = async () => {
      try {
        setLoading(true);
        const response = await apiClient.get<Sweet[]>('/sweets');
        setSweets(response.data);
      } catch (err) {
        console.error("Error fetching sweets:", err);
        setError("Failed to load sweets.");
      } finally {
        // Runs whether success or failure, ensuring loading stops
        setLoading(false); 
      }
    };

    fetchSweets();
  }, []);

  const handleAddToCart = async (sweetId: number) => {
    try {
      await apiClient.post("/cart/add", {
        sweetId: sweetId,
        quantity: 1
      });
      alert("Added to cart successfully!");
    } catch (err) {
      console.error("Error adding to cart:", err);
      alert("Failed to add to cart. Are you logged in?");
    }
  };

  if (loading) return <div className="text-center p-10">Loading yummy sweets...</div>;
  if (error) return <div className="text-center text-red-500 p-10">{error}</div>;

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
            <button onClick={() => handleAddToCart(sweet.id)}
              className="mt-4 w-full bg-blue-600 text-white py-2 px-4 rounded hover:bg-blue-700 transition-colors">
              Add to Cart
            </button>
          </div>
        </div>
      ))}
    </div>
  );
}