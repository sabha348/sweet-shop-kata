import { BrowserRouter, Routes, Route, Navigate, Outlet, Link } from "react-router-dom";
import Register from "./Register";
import Login from "./Login";
import RequireAuth from "./RequireAuth";
import SweetList from "./SweetList";
import Cart from "./Cart";
import OrderHistory from "./OrderHistory";

function AuthenticatedLayout() {
  return (
    <div className="min-h-screen bg-gray-50">
      {/* Navigation Header */}
      <header className="bg-white shadow p-4 sticky top-0 z-50">
        <div className="max-w-7xl mx-auto flex justify-between items-center">
          
          <div className="flex items-center gap-6">
            <h1 className="text-2xl font-bold text-red-600">
              <Link to="/">Sweet Shop 🍬</Link>
            </h1>
            {/* Navigation Links */}
            <nav className="hidden md:flex gap-4 text-gray-600 font-medium">
              <Link to="/" className="hover:text-red-500 transition-colors">Home</Link>
              <Link to="/cart" className="hover:text-red-500 transition-colors">Cart 🛒</Link>
              <Link to="/orders" className="hover:text-red-500 transition-colors">My Orders 📦</Link>
            </nav>
          </div>

          <div className="flex items-center gap-4">
            <span className="text-sm text-gray-500 hidden sm:inline">Securely logged in</span>
            <button
              onClick={() => {
                localStorage.removeItem("jwt_token");
                window.location.reload();
              }}
              className="py-2 px-4 bg-red-500 text-white rounded hover:bg-red-600 transition-colors"
            >
              Logout
            </button>
          </div>
        </div>
      </header>

      {/* This renders the actual page content (SweetList or Cart) */}
      <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <Outlet /> 
      </main>
    </div>
  );
}

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Public Routes */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* Protected Routes - Note how we wrap Dashboard inside RequireAuth */}
        <Route
          path="/"
          element={
            <RequireAuth>
              <AuthenticatedLayout />
            </RequireAuth>
          }>
            <Route index element={<SweetList />} />
            <Route path="cart" element={<Cart />} />
            <Route path="/orders" element={<OrderHistory />} />
          </Route>

        {/* Catch-all */}
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
