import React, { useState, useEffect } from 'react';
import { getPlanes, deletePlan } from '../../services/planService';
import CreatePlanForm from './CreatePlanForm';
import PlanSearch from './PlanSearch';
import EditPlanModal from './EditPlanModal';
import DeleteConfirmationModal from './DeleteConfirmationModal';
import { FaPlus, FaEdit, FaTrash, FaSearch } from 'react-icons/fa';
import './PlanesList.css';

const PlanesList = () => {
  const [planes, setPlanes] = useState([]);
  const [filteredPlanes, setFilteredPlanes] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [editingPlan, setEditingPlan] = useState(null);
  const [deletingPlan, setDeletingPlan] = useState(null);
  const [isDeleting, setIsDeleting] = useState(false);

  const fetchPlanes = async () => {
    try {
      const data = await getPlanes();
      setPlanes(data);
      setLoading(false);
    } catch (err) {
      setError(err.message);
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPlanes();
  }, []);

  const handlePlanCreated = () => {
    fetchPlanes();
    setShowCreateForm(false);
  };

  const handleSearchResults = (results) => {
    setFilteredPlanes(results);
  };

  const handleUpdatePlan = (updatedPlan) => {
    setPlanes(planes.map(p =>
      p.id === updatedPlan.id ? updatedPlan : p
    ));

    if (filteredPlanes) {
      setFilteredPlanes(filteredPlanes.map(p =>
        p.id === updatedPlan.id ? updatedPlan : p
      ));
    }
  };

  const handleDeletePlan = async (id) => {
    setIsDeleting(true);
    try {
      await deletePlan(id);
      setPlanes(planes.filter(p => p.id !== id));

      if (filteredPlanes) {
        setFilteredPlanes(filteredPlanes.filter(p => p.id !== id));
      }

      setDeletingPlan(null);
    } catch (error) {
      console.error('Error al eliminar plan:', error);
    } finally {
      setIsDeleting(false);
    }
  };

  const displayedPlanes = filteredPlanes || planes;

  if (loading) return <div className="loading">Cargando planes...</div>;
  if (error) return <div className="error">Error: {error}</div>;

  return (
    <div className="planes-container">
      <div className="actions-header">
        <PlanSearch onSearchResults={handleSearchResults} />

        <button
          onClick={() => setShowCreateForm(!showCreateForm)}
          className="primary-button"
        >
          <FaPlus /> {showCreateForm ? 'Cancelar' : 'Nuevo Plan'}
        </button>
      </div>

      {showCreateForm && (
        <CreatePlanForm onPlanCreated={handlePlanCreated} />
      )}

      {editingPlan && (
        <EditPlanModal
          plan={editingPlan}
          onClose={() => setEditingPlan(null)}
          onUpdate={handleUpdatePlan}
        />
      )}

      {deletingPlan && (
        <DeleteConfirmationModal
          item={deletingPlan}
          itemType="plan"
          onClose={() => setDeletingPlan(null)}
          onConfirm={handleDeletePlan}
        />
      )}

      <div className="card-container">
        {loading ? (
          <div className="loading-container">
            <div className="loading-spinner"></div>
            <p>Cargando planes...</p>
          </div>
        ) : error ? (
          <div className="error-message">
            Error al cargar los planes: {error}
          </div>
        ) : displayedPlanes.length > 0 ? (
          <div className="responsive-table">
            <table className="planes-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Descripción</th>
                  <th>Duración</th>
                  <th>Precio Regular</th>
                  <th>Precio Fin de Semana</th>
                  <th>Precio Feriado</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {displayedPlanes.map((plan) => (
                  <tr key={plan.id}>
                    <td>{plan.id}</td>
                    <td>{plan.descripcion}</td>
                    <td>{plan.duracionTotal} min</td>
                    <td>${plan.precioRegular.toLocaleString()}</td>
                    <td>${plan.precioFinSemana.toLocaleString()}</td>
                    <td>${plan.precioFeriado.toLocaleString()}</td>
                    <td className="actions-cell">
                      <button
                        onClick={() => setEditingPlan(plan)}
                        className="icon-button edit"
                        title="Editar"
                      >
                        <FaEdit />
                      </button>
                      <button
                        onClick={() => setDeletingPlan(plan)}
                        className="icon-button delete"
                        title="Eliminar"
                        disabled={isDeleting}
                      >
                        <FaTrash />
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          <div className="no-results">
            {filteredPlanes ? 'No se encontraron planes' : 'No hay planes registrados'}
          </div>
        )}
      </div>
    </div>
  );
};

export default PlanesList;