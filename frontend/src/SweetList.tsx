import { useEffect, useState } from "react";
import apiClient from "./api/axiosClient";

interface Sweet {
  id: number;
  name: string;
  price: number;
  imageUrl: string;
  quantity: number;
  category: string;
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
        const response = await apiClient.get<Sweet[]>("/sweets");
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
        quantity: 1,
      });
      alert("Added to cart successfully!");
    } catch (err) {
      console.error("Error adding to cart:", err);
      alert("Failed to add to cart. Are you logged in?");
    }
  };

  if (loading)
    return <div className="text-center p-10">Loading yummy sweets...</div>;
  if (error)
    return <div className="text-center text-red-500 p-10">{error}</div>;

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 p-6">
      {sweets.map((sweet) => (
        <div
          key={sweet.id}
          className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow"
        >
          <img
            src={sweet.imageUrl}
            alt={sweet.name}
            className="w-full h-48 object-cover"
          />
          <div className="p-4">
            <div className="flex justify-between items-start">
              <h3 className="text-xl font-semibold text-gray-800">
                {sweet.name}
              </h3>
              {/* 🟢 Category Badge */}
              <span className="text-xs bg-purple-100 text-purple-800 px-2 py-1 rounded-full">
                {sweet.category}
              </span>
            </div>
            <p className="text-gray-600 mt-2">₹{sweet.price}</p>

            {/* 🟢 Stock Status Display */}
            <p
              className={`text-sm mt-1 ${
                sweet.quantity > 0 ? "text-green-600" : "text-red-600 font-bold"
              }`}
            >
              {sweet.quantity > 0
                ? `In Stock: ${sweet.quantity}`
                : "Out of Stock"}
            </p>

            {/* 🟢 Disabled Button if Out of Stock */}
            <button
              onClick={() => handleAddToCart(sweet.id)}
              disabled={sweet.quantity === 0}
              className={`mt-4 w-full py-2 px-4 rounded transition-colors ${
                sweet.quantity > 0
                  ? "bg-blue-600 text-white hover:bg-blue-700"
                  : "bg-gray-300 text-gray-500 cursor-not-allowed"
              }`}
            >
              {sweet.quantity > 0 ? "Add to Cart" : "Sold Out"}
            </button>
          </div>
        </div>
      ))}
    </div>
  );
}
