import React from 'react';
import PlanesList from '../../components/planes/PlanesList';
import PlanesPage from './PlanesPage';
import './Planes.css';

const Planes = () => {
  return (
    <div className="planes-page">
      <div className="page-header">
        <h1 className="page-title">
          <span className="title-text">Administración de</span>
          <span className="highlight">Planes</span>
        </h1>
        <p className="page-description">
          Crea, edita y gestiona los planes disponibles para tus clientes
        </p>
      </div>
      <PlanesPage />
    </div>
  );
};

export default Planes;