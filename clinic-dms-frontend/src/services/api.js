const BASE_URL = "http://localhost:8080/api/documents";

export const uploadDocument = (patientId, file) => {
    const formData = new FormData();
    formData.append("file", file);
    return fetch(`${BASE_URL}/upload/${patientId}`, {method: "POST", body: formData});
}

export const getDocuments = (patientId) => {
    return fetch( `${BASE_URL}/${patientId}`).then(res => res.json())
}

export const deleteDocument = (key) => {
    return fetch(`${BASE_URL}/delete?key=${encodeURIComponent(key)}`, {method: "DELETE"});
} 

export const getPresignedUrl = (key) => {
    return fetch(`${BASE_URL}/view?key=${encodeURIComponent(key)}`).then(res => res.text());    
}