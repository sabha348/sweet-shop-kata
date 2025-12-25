import { useEffect, useState } from "react";
import axiosClient from "./api/axiosClient";

interface OrderItem {
    sweetName: string;
    quantity: number;
    priceAtPurchase: number;
}

interface Order {
    orderId: number;
    orderDate: string;
    totalPrice: number;
    items: OrderItem[];
}

export default function OrderHistory() {
    const [orders, setOrders] = useState<Order[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const fetchOrders = async () => {
            try {
                const response = await axiosClient.get<Order[]>("/orders");
                setOrders(response.data);
            } catch (err) {
                console.error("Error fetching orders:", err);
                setError("Failed to load order history.");
            } finally {
                setLoading(false);
            }
        };

        fetchOrders();
    }, []);

    if (loading) return <div className="text-center mt-10 text-lg">Loading your history...</div>;
    if (error) return <div className="text-center mt-10 text-red-500">{error}</div>;

    if (orders.length === 0) {
        return (
            <div className="text-center mt-20">
                <h2 className="text-2xl font-bold text-gray-700">No past orders yet 🤷‍♂️</h2>
                <p className="text-gray-500 mt-2">Time to buy some sweets!</p>
            </div>
        );
    }

    return (
        <div className="max-w-4xl mx-auto mt-10 p-6">
            <h2 className="text-3xl font-bold text-gray-800 mb-8 border-b pb-4">My Past Orders</h2>
            
            <div className="space-y-6">
                {orders.map((order) => (
                    <div key={order.orderId} className="bg-white shadow-md rounded-lg overflow-hidden border border-gray-100">
                        {/* Order Header */}
                        <div className="bg-gray-50 px-6 py-4 flex justify-between items-center border-b">
                            <div>
                                <p className="text-sm text-gray-500 uppercase tracking-wide font-semibold">Order Placed</p>
                                <p className="text-gray-800 font-medium">{order.orderDate}</p>
                            </div>
                            <div className="text-right">
                                <p className="text-sm text-gray-500 uppercase tracking-wide font-semibold">Total</p>
                                <p className="text-lg font-bold text-blue-600">₹{order.totalPrice}</p>
                            </div>
                        </div>

                        {/* Order Items */}
                        <div className="px-6 py-4">
                            <ul className="divide-y divide-gray-100">
                                {order.items.map((item, index) => (
                                    <li key={index} className="py-3 flex justify-between">
                                        <div>
                                            <span className="font-semibold text-gray-700">{item.sweetName}</span>
                                            <span className="text-gray-500 text-sm ml-2">x {item.quantity}</span>
                                        </div>
                                        <span className="text-gray-600">₹{item.priceAtPurchase * item.quantity}</span>
                                    </li>
                                ))}
                            </ul>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}