import { useState } from "react";
import axiosClient from "./api/axiosClient";
import { useNavigate } from "react-router-dom";

export default function AdminDashboard() {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        name: "",
        price: "",
        quantity: "",
        imageUrl: ""
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await axiosClient.post("/sweets", {
                name: formData.name,
                price: Number(formData.price),     // Convert to number
                quantity: Number(formData.quantity), // Convert to number
                imageUrl: formData.imageUrl
            });
            alert("Sweet added successfully! 🍬");
            navigate("/"); // Go back home to see it
        } catch (error) {
            console.error("Failed to add sweet", error);
            alert("Failed to add sweet. Are you an Admin?");
        }
    };

    return (
        <div className="max-w-md mx-auto mt-10 p-6 bg-white shadow-lg rounded-lg border border-gray-200">
            <h2 className="text-2xl font-bold text-gray-800 mb-6 text-center">Add New Sweet</h2>
            
            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <label className="block text-sm font-medium text-gray-700">Sweet Name</label>
                    <input name="name" required onChange={handleChange} className="w-full p-2 border rounded mt-1" placeholder="e.g. Kaju Katli" />
                </div>
                
                <div className="grid grid-cols-2 gap-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Price (₹)</label>
                        <input name="price" type="number" required onChange={handleChange} className="w-full p-2 border rounded mt-1" placeholder="100" />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Quantity</label>
                        <input name="quantity" type="number" required onChange={handleChange} className="w-full p-2 border rounded mt-1" placeholder="50" />
                    </div>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700">Image URL</label>
                    <input name="imageUrl" onChange={handleChange} className="w-full p-2 border rounded mt-1" placeholder="https://example.com/image.jpg" />
                </div>

                <button type="submit" className="w-full bg-red-600 text-white py-2 rounded hover:bg-red-700 font-bold transition-colors">
                    Add to Shop
                </button>
            </form>
        </div>
    );
}