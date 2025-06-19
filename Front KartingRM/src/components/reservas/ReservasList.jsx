import React, { useState, useEffect } from 'react';
import { getReservas, deleteReserva, getIntegrantes } from '../../services/reservaService';
import CreateReservaForm from './CreateReservaForm';
import ReservaSearch from './ReservaSearch';
import EditReservaModal from './EditReservaModal';
import IntegrantesModal from './IntegrantesModal';
import DeleteConfirmationModal from './DeleteConfirmationModal';
import './ReservasList.css';

const ReservasList = () => {
  const [reservas, setReservas] = useState([]);
  const [filteredReservas, setFilteredReservas] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [editingReserva, setEditingReserva] = useState(null);
  const [viewingIntegrantes, setViewingIntegrantes] = useState(null);
  const [deletingReserva, setDeletingReserva] = useState(null);
  const [isDeleting, setIsDeleting] = useState(false);
  const [integrantes, setIntegrantes] = useState([]);
  const [loadingIntegrantes, setLoadingIntegrantes] = useState(false);

  const fetchReservas = async () => {
    try {
      const data = await getReservas();
      setReservas(data);
      setLoading(false);
    } catch (err) {
      setError(err.message);
      setLoading(false);
    }
  };

  const fetchIntegrantes = async (idReserva) => {
    setLoadingIntegrantes(true);
    try {
      const data = await getIntegrantes(idReserva);
      setIntegrantes(data);
      setLoadingIntegrantes(false);
    } catch (err) {
      console.error('Error al obtener integrantes:', err);
      setLoadingIntegrantes(false);
    }
  };

  useEffect(() => {
    fetchReservas();
  }, []);

  useEffect(() => {
    if (viewingIntegrantes) {
      fetchIntegrantes(viewingIntegrantes.id);
    }
  }, [viewingIntegrantes]);

  const handleReservaCreated = () => {
    fetchReservas();
    setShowCreateForm(false);
  };

  const handleSearchResults = (results) => {
    setFilteredReservas(results);
  };

  const handleUpdateReserva = (updatedReserva) => {
    setReservas(reservas.map(r =>
      r.id === updatedReserva.id ? updatedReserva : r
    ));

    if (filteredReservas) {
      setFilteredReservas(filteredReservas.map(r =>
        r.id === updatedReserva.id ? updatedReserva : r
      ));
    }
  };

  const handleDeleteReserva = async (id) => {
    setIsDeleting(true);
    try {
      await deleteReserva(id);
      setReservas(reservas.filter(r => r.id !== id));

      if (filteredReservas) {
        setFilteredReservas(filteredReservas.filter(r => r.id !== id));
      }

      setDeletingReserva(null);
    } catch (error) {
      console.error('Error al eliminar reserva:', error);
    } finally {
      setIsDeleting(false);
    }
  };

  const displayedReservas = filteredReservas || reservas;

  if (loading) return <div className="loading">Cargando reservas...</div>;
  if (error) return <div className="error">Error: {error}</div>;

  return (
    <div className="reservas-container">
      <div className="reservas-header">
        <h2>Lista de Reservas</h2>
        <button
          onClick={() => setShowCreateForm(!showCreateForm)}
          className="create-button"
        >
          {showCreateForm ? 'Cancelar' : 'Crear Nueva Reserva'}
        </button>
      </div>

      <ReservaSearch onSearchResults={handleSearchResults} />

      {showCreateForm && (
        <CreateReservaForm onReservaCreated={handleReservaCreated} />
      )}

      {editingReserva && (
        <EditReservaModal
          reserva={editingReserva}
          onClose={() => setEditingReserva(null)}
          onUpdate={handleUpdateReserva}
        />
      )}

      {viewingIntegrantes && (
        <IntegrantesModal
          reserva={viewingIntegrantes}
          integrantes={integrantes}
          loading={loadingIntegrantes}
          onClose={() => setViewingIntegrantes(null)}
        />
      )}

      {deletingReserva && (
        <DeleteConfirmationModal
          item={deletingReserva}
          itemType="reserva"
          onClose={() => setDeletingReserva(null)}
          onConfirm={() => handleDeleteReserva(deletingReserva.id)}
        />
      )}

      <table className="reservas-table">
        <thead>
          <tr>
            <th>ID Reserva</th>
            <th>Fecha</th>
            <th>Hora Inicio</th>
            <th>Hora Fin</th>
            <th>Estado</th>
            <th>Personas</th>
            <th>Plan</th>
            <th>Cliente</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {displayedReservas.length > 0 ? (
            displayedReservas.map((reserva) => (
              <tr key={reserva.id}>
                <td>{reserva.id}</td>
                <td>{new Date(reserva.fecha + "T00:00:00").toLocaleDateString()}</td>
                <td>{reserva.horaInicio}</td>
                <td>{reserva.horaFin || '-'}</td>
                <td>
                  <span className={`status-badge ${reserva.estado.toLowerCase()}`}>
                    {reserva.estado}
                  </span>
                </td>
                <td>{reserva.totalPersonas}</td>
                <td>
                  {reserva.plan.id} - {reserva.plan.descripcion}
                </td>
                <td>
                  {reserva.reservante.id} - {reserva.reservante.nombre} {reserva.reservante.apellido}
                </td>
                <td className="actions-cell">
                  <button
                    onClick={() => setEditingReserva(reserva)}
                    className="edit-button"
                  >
                    Editar
                  </button>
                  <button
                    onClick={() => setViewingIntegrantes(reserva)}
                    className="view-button"
                  >
                    Integrantes
                  </button>
                  <button
                    onClick={() => setDeletingReserva(reserva)}
                    className="delete-button"
                    disabled={isDeleting}
                  >
                    Eliminar
                  </button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="9" className="no-results">
                {filteredReservas ? 'No se encontraron reservas' : 'No hay reservas registradas'}
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default ReservasList;