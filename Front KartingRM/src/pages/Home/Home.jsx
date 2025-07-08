import './Home.css'
import { useNavigate } from 'react-router-dom'
import {
  FaUsers,
  FaCalendarAlt,
  FaFileInvoice,
  FaChartBar,
  FaCalendarWeek,
  FaPlus,
  FaCog,
  FaMoneyBillWave,
  FaClipboardList,
  FaClipboard
} from 'react-icons/fa'
import { FaChartLine } from 'react-icons/fa6'
import { useEffect, useState } from 'react'
import httpClient from '../../http-common';

export default function Home() {
  const navigate = useNavigate()
  const [stats, setStats] = useState({
    reservas: { hoy: 0, ayer: 0 },
    ingresos: { mesActual: 0, mesAnterior: 0 }
  })
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchStats = async () => {
      try {
        // Obtener datos de reservas
        const reservasRes = await httpClient.get('/api/reservas-comprobantes-service/reservas/contador-diario')

        // Obtener datos de ingresos
        const ingresosRes = await httpClient.get('/api/reportes-service/segun-plan/ingresos-totales-mes')

        setStats({
          reservas: {
            hoy: reservasRes.data.reservasHoy,
            ayer: reservasRes.data.reservasAyer
          },
          ingresos: {
            mesActual: ingresosRes.data.ingresosMesActual,
            mesAnterior: ingresosRes.data.ingresosMesAnterior
          }
        })
      } catch (error) {
        console.error('Error fetching stats:', error)
      } finally {
        setLoading(false)
      }
    }

    fetchStats()
  }, [])

  // Calcular diferencia y porcentaje para reservas
  const diffReservas = stats.reservas.hoy - stats.reservas.ayer
  const porcentajeReservas = stats.reservas.ayer !== 0
    ? ((diffReservas / stats.reservas.ayer) * 100).toFixed(1)
    : diffReservas > 0 ? '100' : '0'

  // Calcular diferencia y porcentaje para ingresos
  const diffIngresos = stats.ingresos.mesActual - stats.ingresos.mesAnterior
  const porcentajeIngresos = stats.ingresos.mesAnterior !== 0
    ? ((diffIngresos / stats.ingresos.mesAnterior) * 100).toFixed(1)
    : diffIngresos > 0 ? '100' : '0'

  // Determinar clase CSS según si es favorable o no
  const getComparisonClass = (difference) => {
    return difference >= 0 ? 'positive' : 'negative'
  }

  const quickActions = [
    { icon: <FaUsers size={24} />, label: 'Clientes', action: () => navigate('/clientes') },
    { icon: <FaCalendarAlt size={24} />, label: 'Reservas', action: () => navigate('/reservasv1') },
    { icon: <FaFileInvoice size={24} />, label: 'Planes', action: () => navigate('/planes') },
    { icon: <FaChartBar size={24} />, label: 'Reportes', action: () => navigate('/reportes') },
    { icon: <FaCalendarWeek size={24} />, label: 'Rack Semanal', action: () => navigate('/calendario') },
    { icon: <FaCog size={24} />, label: 'Configuración', action: () => navigate('/configuraciones') }
  ]

  const commonTasks = [
    {
      question: "¿Quieres crear una nueva reserva?",
      icon: <FaCalendarAlt className="task-icon" />,
      actions: [
        { label: "Crear nueva reserva", icon: <FaPlus />, action: () => navigate('/reservasv1') }
      ]
    },
    {
      question: "¿Necesitas gestionar clientes?",
      icon: <FaUsers className="task-icon" />,
      actions: [
        { label: "Crear nuevo cliente", icon: <FaPlus />, action: () => navigate('/clientes') }
      ]
    },
    {
      question: "¿Qué reportes necesitas ver?",
      icon: <FaChartBar className="task-icon" />,
      actions: [
        { label: "Generar reportes", icon: <FaChartLine />, action: () => navigate('/reportes') }
      ]
    },
    {
      question: "¿Quieres ver las reservas programadas?",
      icon: <FaCalendarWeek className="task-icon" />,
      actions: [
        { label: "Ver rack semanal", icon: <FaCalendarWeek />, action: () => navigate('/calendario') }
      ]
    },
    {
      question: "¿Necesitas ver los planes disponibles?",
      icon: <FaClipboardList className="task-icon" />,
      actions: [
        { label: "Ver todos los planes", icon: <FaClipboard />, action: () => navigate('/planes') }
      ]
    }
  ]

  return (
    <div className="home-container">
      <div className="content-wrapper">
        <div className="welcome-section">
          <div className="welcome-title-container">
            <h1 className="welcome-title">
              <span className="welcome-text">Bienvenido al sistema de</span>
              <span className="company-name">Karting RM</span>
            </h1>
            <div className="title-decoration"></div>
          </div>
          <p className="subtitle">Gestión integral de reservas, comprobantes, clientes, reportes financieros y más</p>
        </div>

        {/* Sección de preguntas interactivas */}
        <div className="interactive-section">
          <h2><span className="highlight">¿Qué quieres hacer?</span></h2>
          <div className="task-list">
            {commonTasks.map((task, index) => (
              <div key={index} className="task-card">
                <div className="task-header">
                  {task.icon}
                  <h3>{task.question}</h3>
                </div>
                <div className="task-actions">
                  {task.actions.map((action, actionIndex) => (
                    <button
                      key={actionIndex}
                      onClick={action.action}
                      className="task-btn"
                    >
                      {action.icon && <span className="btn-icon">{action.icon}</span>}
                      {action.label}
                    </button>
                  ))}
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="quick-actions">
          <h2><span className="highlight">Accesos rápidos</span></h2>
          <div className="action-buttons">
            {quickActions.map((item, index) => (
              <button key={index} onClick={item.action} className="action-btn">
                <div className="action-icon">{item.icon}</div>
                <span>{item.label}</span>
              </button>
            ))}
          </div>
        </div>

        <div className="stats-preview">
          <h2><span className="highlight">Resumen rápido</span></h2>
          <div className="stats-grid">
            {/* Card de Reservas */}
            <div className="stat-card">
              <div className="stat-icon">
                <FaCalendarAlt />
              </div>
              <h3>Reservas hoy</h3>
              {loading ? (
                <p className="stat-value loading">Cargando...</p>
              ) : (
                <>
                  <p className="stat-value">{stats.reservas.hoy}</p>
                  <p className={`stat-comparison ${getComparisonClass(diffReservas)}`}>
                    {diffReservas >= 0 ? '+' : ''}{diffReservas} vs ayer ({porcentajeReservas}%)
                  </p>
                </>
              )}
            </div>

            {/* Card de Ingresos */}
            <div className="stat-card">
              <div className="stat-icon">
                <FaMoneyBillWave />
              </div>
              <h3>Ingresos mensuales</h3>
              {loading ? (
                <p className="stat-value loading">Cargando...</p>
              ) : (
                <>
                  <p className="stat-value">${stats.ingresos.mesActual.toLocaleString()}</p>
                  <p className={`stat-comparison ${getComparisonClass(diffIngresos)}`}>
                    {diffIngresos >= 0 ? '+' : ''}${Math.abs(diffIngresos).toLocaleString()} ({porcentajeIngresos}%) vs mes pasado
                  </p>
                </>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}