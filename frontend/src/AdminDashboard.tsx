import { useState, useEffect } from "react";
import apiClient from "./api/axiosClient"; // 🟢 Use the centralized client

// Define the shape of a Sweet object
interface Sweet {
  id?: number;
  name: string;
  price: string;    
  quantity: string; // 🟢 Admin still needs to manage quantity
  imageUrl: string;
  category: string;
}

export default function AdminDashboard() {
  // State for the form
  const [sweet, setSweet] = useState<Sweet>({ name: "", price: "", quantity: "", imageUrl: "", category: "" });
  // State for the list of sweets
  const [sweetsList, setSweetsList] = useState<Sweet[]>([]);
  // State to track if we are editing (holds the ID being edited)
  const [editingId, setEditingId] = useState<number | null>(null);

  // Fetch all sweets on load
  useEffect(() => {
    fetchSweets();
  }, []);

  const fetchSweets = async () => {
    try {
      // 🟢 Uses apiClient (Base URL is handled automatically)
      const response = await apiClient.get<Sweet[]>("/sweets");
      setSweetsList(response.data);
    } catch (error) {
      console.error("Error fetching sweets:", error);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSweet({ ...sweet, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    // Prepare data (convert strings to numbers for backend)
    const payload = {
        ...sweet,
        price: Number(sweet.price),
        quantity: Number(sweet.quantity)
    };

    try {
      if (editingId) {
        // --- UPDATE MODE ---
        // 🟢 Uses apiClient (Token is likely handled by interceptor)
        await apiClient.put(`/sweets/${editingId}`, payload);
        alert("Sweet Updated Successfully!");
        setEditingId(null); 
      } else {
        // --- CREATE MODE ---
        await apiClient.post("/sweets", payload);
        alert("Sweet Added Successfully!");
      }

      // Reset Form & Refresh List
      setSweet({ name: "", price: "", quantity: "", imageUrl: "", category: "" });
      fetchSweets();

    } catch (error) {
      console.error("Operation failed", error);
      alert("Action failed! Check console.");
    }
  };

  // Populate form for editing
  const handleEdit = (sweetToEdit: Sweet) => {
    setEditingId(sweetToEdit.id!); 
    setSweet({ 
        name: sweetToEdit.name, 
        price: String(sweetToEdit.price),       
        quantity: String(sweetToEdit.quantity), 
        imageUrl: sweetToEdit.imageUrl, 
        category: sweetToEdit.category
    });
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm("Are you sure you want to delete this sweet?")) return;

    try {
      // 🟢 Uses apiClient
      await apiClient.delete(`/sweets/${id}`);
      alert("Sweet Deleted!");
      fetchSweets(); 
    } catch (error) {
      console.error("Delete failed", error);
      alert("Delete failed!");
    }
  };

  return (
    <div className="p-8 max-w-4xl mx-auto">
      <h2 className="text-3xl font-bold mb-6 text-center text-purple-700">Admin Dashboard</h2>

      {/* --- FORM SECTION --- */}
      <div className="bg-white p-6 rounded-lg shadow-md mb-8">
        <h3 className="text-xl font-semibold mb-4">
            {editingId ? "Edit Sweet" : "Add New Sweet"}
        </h3>
        <form onSubmit={handleSubmit} className="space-y-4">
          <input
            type="text"
            name="name"
            placeholder="Sweet Name"
            value={sweet.name}
            onChange={handleChange}
            className="w-full p-2 border rounded"
            required
          />

          <input
            type="text"
            name="category"
            placeholder="Category (e.g., Milk-Based)"
            value={sweet.category}
            onChange={handleChange}
            className="w-full p-2 border rounded"
            required
          />

          <div className="flex gap-4">
            <input
                type="number"
                name="price"
                placeholder="Price (₹)"
                value={sweet.price}
                onChange={handleChange}
                className="w-1/2 p-2 border rounded"
                required
            />
            <input
                type="number"
                name="quantity"
                placeholder="Quantity (Stock)"
                value={sweet.quantity}
                onChange={handleChange}
                className="w-1/2 p-2 border rounded"
                required
            />
          </div>
          <input
            type="text"
            name="imageUrl"
            placeholder="Image URL"
            value={sweet.imageUrl}
            onChange={handleChange}
            className="w-full p-2 border rounded"
            required
          />
          <div className="flex gap-2">
            <button
                type="submit"
                className={`flex-1 text-white p-2 rounded ${
                    editingId ? "bg-blue-600 hover:bg-blue-700" : "bg-purple-600 hover:bg-purple-700"
                }`}
            >
                {editingId ? "Update Sweet" : "Add Sweet"}
            </button>
            {editingId && (
                <button 
                    type="button" 
                    onClick={() => { setEditingId(null); setSweet({ name: "", price: "", quantity: "", imageUrl: "", category: "" }); }}
                    className="bg-gray-500 text-white p-2 rounded hover:bg-gray-600"
                >
                    Cancel
                </button>
            )}
          </div>
        </form>
      </div>

      {/* --- LIST SECTION --- */}
      <div className="bg-white p-6 rounded-lg shadow-md">
        <h3 className="text-xl font-semibold mb-4">Manage Sweets</h3>
        <table className="w-full text-left border-collapse">
            <thead>
                <tr className="border-b bg-gray-100">
                    <th className="p-2">Name</th>
                    <th className="p-2">Category</th>
                    <th className="p-2">Price</th>
                    <th className="p-2">Quantity</th>
                    <th className="p-2">Actions</th>
                </tr>
            </thead>
            <tbody>
                {sweetsList.map((s) => (
                    <tr key={s.id} className="border-b hover:bg-gray-50">
                        <td className="p-2">{s.name}</td>
                        <td className="p-2 text-sm text-gray-600">{s.category}</td>
                        <td className="p-2">₹{s.price}</td>
                        <td className="p-2">{s.quantity}</td>
                        <td className="p-2 space-x-2">
                            <button 
                                onClick={() => handleEdit(s)}
                                className="text-blue-600 hover:text-blue-800 font-medium px-2 py-1 rounded hover:bg-blue-100"
                            >
                                Edit
                            </button>
                            <button 
                                onClick={() => handleDelete(s.id!)}
                                className="text-red-600 hover:text-red-800 font-medium px-2 py-1 rounded hover:bg-red-100"
                            >
                                Delete
                            </button>
                        </td>
                    </tr>
                ))}
            </tbody>
        </table>
      </div>
    </div>
  );
}