import httpClient from '../http-common';

const URL_LOCAL = '/api/plan/planes';

// Peticion GET de todos los planes
export const getPlanes = async () => {
  try {
    const response = await httpClient.get(`${URL_LOCAL}/`);
    return response.data;
  } catch (error) {
    console.error('Error al obtener planes:', error);
    throw error;
  }
};

/// Peticion GET de plan segun id
export const getPlanById = async (id) => {
  try {
    const response = await httpClient.get(`${URL_LOCAL}/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error al obtener plan con ID ${id}:`, error);
    throw error;
  }
};

// Peticion POST para crear plan
export const createPlan = async (planData) => {
  try {
    const response = await httpClient.post(`${URL_LOCAL}/`, planData);
    return response.data;
  } catch (error) {
    console.error('Error al crear plan:', error);
    throw error;
  }
};

// Peticion PUT para update de plan, segun id y body
export const updatePlan = async (id, planData) => {
  try {
    const response = await httpClient.put(`${URL_LOCAL}/${id}`, planData);
    return response.data;
  } catch (error) {
    console.error(`Error al actualizar plan con ID ${id}:`, error);
    throw error;
  }
};

// Peticion DELETE para eliminar plan
export const deletePlan = async (id) => {
  try {
    const response = await httpClient.delete(`${URL_LOCAL}/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error al eliminar plan con ID ${id}:`, error);
    throw error;
  }
};