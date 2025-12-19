import type { ReactNode } from 'react';
import { Navigate, useLocation } from 'react-router-dom';

export default function RequireAuth({ children }: { children: ReactNode }) {
  const token = localStorage.getItem('jwt_token');
  const location = useLocation();

  if (!token) {
    // Redirect them to the /login page, but save the current location they were
    // trying to go to when they were redirected.
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  return children;
}