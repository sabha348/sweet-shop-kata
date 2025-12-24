import { useEffect, useState } from "react";
import axiosClient from "./api/axiosClient";
import { useNavigate } from "react-router-dom";

interface CartItem {
    id: number; // This is the Sweet ID
    sweetName: string;
    price: number;
    quantity: number;
    totalPrice: number;
}

interface CartResponse {
    id: number;
    items: CartItem[];
    grandTotal: number;
}

export default function Cart() {
    const [cart, setCart] = useState<CartResponse | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const navigate = useNavigate();

    useEffect(() => {
        fetchCart();
    }, []);

    const fetchCart = async () => {
        try {
            const response = await axiosClient.get<CartResponse>("/cart");
            setCart(response.data);
        } catch (err) {
            console.error("Error fetching cart:", err);
            setError("Failed to load cart.");
        } finally {
            setLoading(false);
        }
    };

    const handleCheckout = async () => {
        const confirm = window.confirm(`Ready to pay ₹${cart?.grandTotal}?`);
        if (!confirm) return;

        try {
            await axiosClient.post("/orders/checkout");
            alert("🎉 Order Placed Successfully! Your sweets are on the way.");
            
            // Refresh cart (it should now be empty)
            fetchCart();
            
            // Optional: Redirect to Home or Order History (later)
            navigate("/"); 
        } catch (err) {
            console.error("Checkout failed:", err);
            alert("Checkout failed. Please try again.");
        }
    };

    if (loading) return <div className="text-center mt-10 text-lg">Loading your cart...</div>;
    if (error) return <div className="text-center mt-10 text-red-500">{error}</div>;

    if (!cart || cart.items.length === 0) {
        return (
            <div className="text-center mt-20">
                <h2 className="text-2xl font-bold text-gray-700">Your cart is empty 😔</h2>
                <a href="/" className="text-blue-600 hover:underline mt-4 block">Go back to Sweets</a>
            </div>
        );
    }

    return (
        <div className="max-w-4xl mx-auto mt-10 p-6 bg-white shadow-lg rounded-lg">
            <h2 className="text-3xl font-bold text-gray-800 mb-6 border-b pb-4">Your Shopping Cart</h2>
            
            <table className="w-full text-left border-collapse">
                <thead>
                    <tr className="text-gray-500 border-b">
                        <th className="py-3">Sweet</th>
                        <th className="py-3">Price</th>
                        <th className="py-3 text-center">Quantity</th>
                        <th className="py-3 text-right">Total</th>
                    </tr>
                </thead>
                <tbody>
                    {cart.items.map((item) => (
                        <tr key={item.id} className="border-b last:border-0 hover:bg-gray-50">
                            <td className="py-4 font-semibold text-gray-800">{item.sweetName}</td>
                            <td className="py-4 text-gray-600">₹{item.price}</td>
                            <td className="py-4 text-center">{item.quantity}</td>
                            <td className="py-4 text-right font-medium">₹{item.totalPrice}</td>
                        </tr>
                    ))}
                </tbody>
            </table>

            <div className="mt-8 text-right">
                <p className="text-xl text-gray-600">Grand Total:</p>
                <p className="text-4xl font-bold text-blue-600">₹{cart.grandTotal}</p>
                <button 
                    onClick={handleCheckout}
                    className="mt-6 bg-green-600 hover:bg-green-700 text-white font-bold py-3 px-8 rounded-lg shadow transition-transform transform hover:scale-105"
                >
                    Pay & Checkout
                </button>
            </div>
        </div>
    );
}