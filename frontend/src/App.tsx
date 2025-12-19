import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Register from "./Register";
import Login from "./Login";
import RequireAuth from "./RequireAuth";
import SweetList from "./SweetList";

// Placeholder Dashboard
function Dashboard() {
  return (
    <div className="p-10 text-center">
      <header className="bg-white shadow p-4 flex justify-between items-center">
        <h1 className="text-3xl font-bold text-gray-800">
          Welcome to Sweet Shop! 🍬
        </h1>
        <p className="mt-4 text-gray-600">You are securely logged in.</p>
        <button
          onClick={() => {
            localStorage.removeItem("jwt_token");
            window.location.reload();
          }}
          className="mt-6 py-2 px-4 bg-red-500 text-white rounded hover:bg-red-600"
        >
          Logout
        </button>
      </header>
      <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <SweetList />
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
              <Dashboard />
            </RequireAuth>
          }
        />

        {/* Catch-all */}
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
