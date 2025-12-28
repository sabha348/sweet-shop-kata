import type { JSX } from "react";
import { Navigate } from "react-router-dom";

export default function RequireAdmin({ children }: { children: JSX.Element }) {
    const role = localStorage.getItem("user_role");

    if (role !== "ROLE_ADMIN") {
        // If not admin, redirect to Home
        return <Navigate to="/" replace />;
    }

    return children;
}