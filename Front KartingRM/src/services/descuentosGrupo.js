import httpClient from '../http-common';

const BASE_URL = '/api/descuento-grupo-service/desc-grupo';

export const getAllGroupDiscounts = async () => {
    try {
        const response = await httpClient.get(`${BASE_URL}/`);
        return response.data;
    } catch (error) {
        console.error("Error al obtener descuentos de grupo", error);
        throw error;
    }
};

export const getGroupDiscountById = async (id) => {
    try {
        const response = await httpClient.get(`${BASE_URL}/${id}`);
        return response.data;
    } catch (error) {
        console.error(`Error al obtener descuento con ID ${id}`, error);
        throw error;
    }
};

export const createGroupDiscount = async (data) => {
    try {
        const response = await httpClient.post(`${BASE_URL}/`, data);
        return response.data;
    } catch (error) {
        console.error('Error al crear descuento de grupo:', error);
        throw error;
    }
};

export const updateGroupDiscount = async (id, data) => {
    try {
        const response = await httpClient.put(`${BASE_URL}/${id}`, data);
        return response.data;
    } catch (error) {
        console.error(`Error al actualizar descuento con ID ${id}:`, error);
        throw error;
    }
};

export const deleteGroupDiscount = async (id) => {
    try {
        await httpClient.delete(`${BASE_URL}/${id}`);
        return id;
    } catch (error) {
        console.error(`Error al eliminar descuento con ID ${id}:`, error);
        throw error;
    }
};

export default {
    getAllGroupDiscounts,
    getGroupDiscountById,
    createGroupDiscount,
    updateGroupDiscount,
    deleteGroupDiscount
};