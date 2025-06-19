import httpClient from '../http-common';

const URL_LOCAL = '/api/reportes-service';

// Peticion Get para obtener los reportes de ingresos segun planes
export const getReporteIngresosPorVueltas = async (params) => {
  try {
    const response = await httpClient.get(`${URL_LOCAL}/segun-plan`, { params });
    return response.data;
  } catch (error) {
    console.error('Error al obtener reporte de ingresos por plan:', error);
    throw error;
  }
};

// Peticion Get para obtener los reportes de ingresos cantidad de personas por reserva
export const getReporteIngresosPorPersonas = async (params) => {
  try {
    const response = await httpClient.get(`${URL_LOCAL}/segun-rango-personas`, { params });
    return response.data;
  } catch (error) {
    console.error('Error al obtener reporte de ingresos por personas:', error);
    throw error;
  }
};