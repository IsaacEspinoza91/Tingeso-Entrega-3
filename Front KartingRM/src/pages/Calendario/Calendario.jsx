import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import { useNavigate } from 'react-router-dom';
import { getReservasSemana } from '../../services/calendarioService';
import { FaArrowLeft, FaArrowRight, FaCalendarDay, FaHome } from 'react-icons/fa';
import './Calendario.css';

const horas = Array.from({ length: 16 }, (_, i) => {
  const hora = 8 + i;
  return `${hora.toString().padStart(2, '0')}:00`;
});

const diasOrden = ['lunes', 'martes', 'miercoles', 'jueves', 'viernes', 'sabado', 'domingo'];

const Calendario = () => {
  const [offset, setOffset] = useState(0);
  const [reservasSemana, setReservasSemana] = useState(null);
  const [fechasDias, setFechasDias] = useState([]);
  const [animarEntrada, setAnimarEntrada] = useState(true);
  const navigate = useNavigate();


  useEffect(() => {
    const cargarReservas = async () => {
      try {
        const data = await getReservasSemana(offset);
        setReservasSemana(data);

        const inicio = new Date(data.fechaInicioSemana + 'T00:00:00Z');
        const fechas = diasOrden.map((_, i) =>
          new Date(Date.UTC(inicio.getUTCFullYear(), inicio.getUTCMonth(), inicio.getUTCDate() + i))
        );
        setFechasDias(fechas);
      } catch (error) {
        console.error('Error al cargar calendario:', error);
      }
    };
    cargarReservas();
  }, [offset]);

  useEffect(() => {
    // Desactiva la animación después de la primera carga
    const timer = setTimeout(() => setAnimarEntrada(false), 600);
    return () => clearTimeout(timer);
  }, []);

  const cambiarSemana = (valor) => setOffset((prev) => prev + valor);
  const volverSemanaActual = () => setOffset(0);

  const formatearFecha = (dateObj) => {
    const dia = dateObj.getUTCDate();
    const mes = dateObj.toLocaleString('es-CL', { month: 'long', timeZone: 'UTC' });
    return `${dia} de ${mes}`;
  };

  return (
    <div className="calendario-page">
      <div className={`calendario-container ${animarEntrada ? 'fade-in' : ''}`}>
        <div className="calendario-header">
          <div className="content-header">
            <button
              onClick={() => navigate('/')}
              className="floating-home-btn"
              aria-label="Volver al inicio"
            >
              <FaHome />
              <span>Inicio</span>
            </button>
            <h1>Calendario de Reservas</h1>
          </div>
        </div>

        <div className="calendario-barra-superior">
          <div className="calendario-fechas">
            Semana del {reservasSemana?.fechaInicioSemana} al {reservasSemana?.fechaFinSemana}
          </div>
          <div className="calendario-controles">
            <button onClick={() => cambiarSemana(-1)}>
              <FaArrowLeft /> Semana anterior
            </button>
            <button onClick={volverSemanaActual} disabled={offset === 0}>
              <FaCalendarDay /> Semana actual
            </button>
            <button onClick={() => cambiarSemana(1)}>
              Semana siguiente <FaArrowRight />
            </button>
          </div>
        </div>

        <div className="calendario-grid-wrapper">
          <div className="calendario-grid">
            <div className="calendario-horas">
              <div className="calendario-dia-header-empty" />
              {horas.map((hora) => (
                <div key={hora} className="hora-cell">{hora}</div>
              ))}
            </div>

            {fechasDias.length === 7 &&
              diasOrden.map((dia, index) => (
                <div key={dia} className="calendario-dia-columna">
                  <div className="calendario-dia-header">
                    <span>{dia.charAt(0).toUpperCase() + dia.slice(1)}</span>
                    <small>{formatearFecha(fechasDias[index])}</small>
                  </div>
                  <div className="calendario-dia-cuerpo">
                    {horas.map((_, i) => (
                      <div key={i} className="hora-linea" />
                    ))}
                    {reservasSemana?.reservasPorDia[dia]?.map((reserva, i) => (
                      <ReservaBlock key={i} reserva={reserva} />
                    ))}
                  </div>
                </div>
              ))}
          </div>
        </div>
      </div>
    </div>
  );
};

const ReservaBlock = ({ reserva }) => {
  const { horaInicio, horaFin, nombreReservante, codigoReserva } = reserva;

  const obtenerPosicionTop = (hora) => {
    const [h, m] = hora.split(':').map(Number);
    return ((h - 8) * 60 + m) * (120 / 60); // para que 1 minuto = 1.83px
  };

  const altura = (new Date(`1970-01-01T${horaFin}`) - new Date(`1970-01-01T${horaInicio}`)) / 60000 * (120 / 60);

  return (
    <div
      className="reserva-block"
      style={{ top: obtenerPosicionTop(horaInicio), height: altura }}
    >
      <strong>#{codigoReserva}</strong> {nombreReservante}<br />
      {horaInicio.substring(0, 5)} - {horaFin.substring(0, 5)}
    </div>
  );
};



ReservaBlock.propTypes = {
  reserva: PropTypes.shape({
    horaInicio: PropTypes.string.isRequired,
    horaFin: PropTypes.string.isRequired,
    nombreReservante: PropTypes.string.isRequired,
    codigoReserva: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired
  }).isRequired
};

export default Calendario;