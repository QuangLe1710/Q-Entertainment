import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import {createBrowserRouter, RouterProvider} from "react-router-dom"
import './index.css'
import HomePage from './pages/HomePage.tsx'
import AuthPage from './pages/AuthPage.tsx'
import NotFoundPage from './pages/NotFoundPage.tsx'
import ProfilesPage from './pages/ProfilesPage.tsx'
import ProfilePage from './pages/ProfilePage.tsx'

const router = createBrowserRouter([
    {
      path: '/',
      element: <HomePage/>,
      errorElement: <NotFoundPage/>
    },
    {
      path: '/authpage',
      element: <AuthPage/>
    },
    {
      path: '/profilespage',
      element: <ProfilesPage/>
    },
    {
      path: 'profilepage/:profileId',
      element: <ProfilePage/>
    }
]);

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>,
)
