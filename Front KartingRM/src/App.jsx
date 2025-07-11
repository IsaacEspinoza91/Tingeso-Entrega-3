import { Routes, Route } from 'react-router-dom'
import Navbar from './components/Navbar/Navbar'
import Home from './pages/Home/Home'
import Clientes from './pages/Clientes/Clientes'
import Planes from './pages/Planes/Planes'
import Calendario from './pages/Calendario/Calendario'
import Reportes from './pages/Reportes/Reportes'
import Configuraciones from './pages/Configuraciones/SettingsMenu'
import ScrollToTop from './components/ScrollToTop'
import Reservations from './pages/Reservation/ReservasPage'
import './App.css'

export default function App() {
  return (
    <div className="app">
      <Navbar />
      <main className="main-content">
        <ScrollToTop />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/clientes" element={<Clientes />} />
          <Route path="/planes" element={<Planes />} />
          <Route path="/calendario" element={<Calendario />} />
          <Route path="/reportes" element={<Reportes />} />
          <Route path="/configuraciones" element={<Configuraciones />} />
          <Route path="/reservasv1" element={<Reservations />} />
        </Routes>
      </main>
    </div>
  )
}