import React, { useState, useEffect } from 'react';
import { addIntegrante, removeIntegrante, getIntegrantes } from '../../services/reservaService';
import './IntegrantesModal.css';

const IntegrantesModal = ({ reserva, onClose }) => {
  const [newIntegranteId, setNewIntegranteId] = useState('');
  const [isAdding, setIsAdding] = useState(false);
  const [isRemoving, setIsRemoving] = useState(false);
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const [integrantes, setIntegrantes] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchIntegrantes = async () => {
      try {
        setLoading(true);
        const data = await getIntegrantes(reserva.id);
        setIntegrantes(data);
      } catch (err) {
        setError('Error al cargar integrantes');
        console.error('Error al obtener integrantes:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchIntegrantes();
  }, [reserva.id]);

  const handleAddIntegrante = async () => {
    if (!newIntegranteId) {
      setError('Por favor ingrese un ID de cliente');
      return;
    }

    setIsAdding(true);
    setError('');
    setSuccessMessage('');

    try {
      await addIntegrante(reserva.id, newIntegranteId);
      // Actualizar la lista de integrantes después de agregar
      const updatedIntegrantes = await getIntegrantes(reserva.id);
      setIntegrantes(updatedIntegrantes);
      setNewIntegranteId('');
      setSuccessMessage('Integrante agregado correctamente');
      setTimeout(() => setSuccessMessage(''), 3000);
    } catch (err) {
      setError(err.response?.data?.message || 'Error al agregar integrante. Verifique el ID.');
    } finally {
      setIsAdding(false);
    }
  };

  const handleRemoveIntegrante = async (idCliente) => {
    setIsRemoving(true);
    setError('');
    setSuccessMessage('');

    try {
      await removeIntegrante(reserva.id, idCliente);
      // Actualizar la lista de integrantes después de quitar
      const updatedIntegrantes = await getIntegrantes(reserva.id);
      setIntegrantes(updatedIntegrantes);
      setSuccessMessage('Integrante eliminado correctamente');
      setTimeout(() => setSuccessMessage(''), 3000);
    } catch (err) {
      setError(err.response?.data?.message || 'Error al quitar integrante');
    } finally {
      setIsRemoving(false);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="integrantes-modal">
        <div className="modal-header">
          <h3>Integrantes de la Reserva #{reserva.id}</h3>
          <button onClick={onClose} className="close-button">&times;</button>
        </div>

        <div className="integrantes-container">
          <div className="add-integrante">
            <input
              type="number"
              value={newIntegranteId}
              onChange={(e) => setNewIntegranteId(e.target.value)}
              placeholder="ID del cliente a agregar"
              min="1"
            />
            <button
              onClick={handleAddIntegrante}
              disabled={isAdding}
              className="add-button"
            >
              {isAdding ? 'Agregando...' : 'Agregar'}
            </button>
          </div>

          {error && <div className="error-message">{error}</div>}
          {successMessage && <div className="success-message">{successMessage}</div>}

          <h4>Integrantes actuales ({integrantes.length}):</h4>

          {loading ? (
            <div className="loading-message">Cargando integrantes...</div>
          ) : (
            <div className="table-container">
              <table className="integrantes-table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>RUT</th>
                    <th>Correo</th>
                    <th>Teléfono</th>
                    <th>Acción</th>
                  </tr>
                </thead>
                <tbody>
                  {integrantes.length > 0 ? (
                    integrantes.map((integrante) => (
                      <tr key={integrante.id}>
                        <td>{integrante.id}</td>
                        <td>{integrante.nombre} {integrante.apellido}</td>
                        <td>{integrante.rut}</td>
                        <td>{integrante.correo}</td>
                        <td>{integrante.telefono}</td>
                        <td>
                          <button
                            onClick={() => handleRemoveIntegrante(integrante.id)}
                            disabled={isRemoving}
                            className="remove-button"
                          >
                            {isRemoving ? 'Quitando...' : 'Quitar'}
                          </button>
                        </td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan="6" className="no-integrantes">
                        No hay integrantes registrados
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          )}
        </div>

        <div className="modal-actions">
          <button type="button" onClick={onClose} className="close-modal-button">
            Cerrar
          </button>
        </div>
      </div>
    </div>
  );
};

export default IntegrantesModal;