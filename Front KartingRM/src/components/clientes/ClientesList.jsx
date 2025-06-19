import React, { useState, useEffect } from 'react';
import {
  getClientes,
  getClientesActivos,
  getClientesInactivos,
  deleteCliente,
  updateCliente,
  desactivarCliente,
  activarCliente
} from '../../services/clienteService';
import CreateClienteForm from './CreateClienteForm';
import ClienteSearch from './ClienteSearch';
import EditClienteModal from './EditClienteModal';
import DeleteConfirmationModal from './DeleteConfirmationModal';
import './ClientesList.css';

const ClientesList = () => {
  const [clientes, setClientes] = useState([]);
  const [filteredClientes, setFilteredClientes] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [editingCliente, setEditingCliente] = useState(null);
  const [deletingCliente, setDeletingCliente] = useState(null);
  const [isDeleting, setIsDeleting] = useState(false);
  const [showInactive, setShowInactive] = useState(false);

  const fetchClientes = async () => {
    try {
      setLoading(true);
      const data = showInactive ? await getClientes() : await getClientesActivos();
      setClientes(data);
      setLoading(false);
    } catch (err) {
      setError(err.message);
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchClientes();
  }, [showInactive]);

  const handleClienteCreated = () => {
    fetchClientes();
    setShowCreateForm(false);
  };

  const handleSearchResults = (results) => {
    setFilteredClientes(results);
  };

  const handleUpdateCliente = (updatedCliente) => {
    setClientes(clientes.map(c =>
      c.id === updatedCliente.id ? updatedCliente : c
    ));

    if (filteredClientes) {
      setFilteredClientes(filteredClientes.map(c =>
        c.id === updatedCliente.id ? updatedCliente : c
      ));
    }
  };

  const handleDeleteCliente = async (id) => {
    setIsDeleting(true);
    try {
      await deleteCliente(id);
      setClientes(clientes.filter(c => c.id !== id));

      if (filteredClientes) {
        setFilteredClientes(filteredClientes.filter(c => c.id !== id));
      }

      setDeletingCliente(null);
    } catch (error) {
      console.error('Error al eliminar cliente:', error);
    } finally {
      setIsDeleting(false);
    }
  };

  const toggleClienteStatus = async (cliente) => {
    try {
      const updatedCliente = cliente.activo
        ? await desactivarCliente(cliente.id)
        : await activarCliente(cliente.id);

      setClientes(clientes.map(c =>
        c.id === cliente.id ? updatedCliente : c
      ));

      if (filteredClientes) {
        setFilteredClientes(filteredClientes.map(c =>
          c.id === cliente.id ? updatedCliente : c
        ));
      }

      console.log(`Cliente ${updatedCliente.nombre} ${updatedCliente.apellido} ha sido ${updatedCliente.activo ? 'activado' : 'desactivado'}`);
    } catch (error) {
      console.error('Error al cambiar estado del cliente:', error);
      console.error('No se pudo cambiar el estado del cliente. Por favor intente nuevamente.');
    }
  };

  const displayedClientes = filteredClientes || clientes;

  if (loading) return <div className="loading">Cargando clientes...</div>;
  if (error) return <div className="error">Error: {error}</div>;

  return (
    <div className="clientes-container">
      <div className="clientes-header">
        <h2>Lista de Clientes</h2>
        <div className="clientes-actions">
          <button
            onClick={() => setShowCreateForm(!showCreateForm)}
            className="create-button"
          >
            {showCreateForm ? 'Cancelar' : 'Crear Nuevo Cliente'}
          </button>
          <button
            onClick={() => setShowInactive(!showInactive)}
            className={`toggle-inactive-button ${showInactive ? 'active' : ''}`}
          >
            {showInactive ? 'Mostrar Solo Activos' : 'Mostrar Todos (Incl. Inactivos)'}
          </button>
        </div>
      </div>

      <ClienteSearch onSearchResults={handleSearchResults} />

      {showCreateForm && (
        <CreateClienteForm onClienteCreated={handleClienteCreated} />
      )}

      {editingCliente && (
        <EditClienteModal
          cliente={editingCliente}
          onClose={() => setEditingCliente(null)}
          onUpdate={handleUpdateCliente}
        />
      )}

      {deletingCliente && (
        <DeleteConfirmationModal
          item={deletingCliente}
          itemType="cliente"
          onClose={() => setDeletingCliente(null)}
          onConfirm={handleDeleteCliente}
          isDeleting={isDeleting}
        />
      )}

      <table className="clientes-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>RUT</th>
            <th>Nombre</th>
            <th>Apellido</th>
            <th>Correo</th>
            <th>Teléfono</th>
            <th>Fecha Nacimiento</th>
            <th>Estado</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {displayedClientes.length > 0 ? (
            displayedClientes.map((cliente) => (
              <tr key={cliente.id}>
                <td>{cliente.id}</td>
                <td>{cliente.rut}</td>
                <td>{cliente.nombre}</td>
                <td>{cliente.apellido}</td>
                <td>{cliente.correo}</td>
                <td>{cliente.telefono}</td>
                <td>{new Date(cliente.fechaNacimiento + "T00:00:00").toLocaleDateString()}</td>
                <td>
                  <span className={`status-badge ${cliente.activo ? 'active' : 'inactive'}`}>
                    {cliente.activo ? 'Activo' : 'Inactivo'}
                  </span>
                </td>
                <td className="actions-cell">
                  <button
                    onClick={() => setEditingCliente(cliente)}
                    className="edit-button"
                  >
                    Editar
                  </button>
                  <button
                    onClick={() => toggleClienteStatus(cliente)}
                    className={`status-button ${cliente.activo ? 'deactivate' : 'activate'}`}
                  >
                    {cliente.activo ? 'Desactivar' : 'Activar'}
                  </button>
                  {!cliente.activo && (
                    <button
                      onClick={() => setDeletingCliente(cliente)}
                      className="delete-button"
                      disabled={isDeleting}
                    >
                      Eliminar
                    </button>
                  )}
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="9" className="no-results">
                {filteredClientes ? 'No se encontraron clientes' : 'No hay clientes registrados'}
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default ClientesList;