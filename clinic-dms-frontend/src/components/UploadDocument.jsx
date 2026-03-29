import React, { useState } from 'react';
import { uploadDocument } from '../services/api';

export default function UploadDocument({patientId, onUploadSuccess}) {
    const [file, setFile] = useState(null);
    const [status, setStatus] = useState("");
    const [loading, setLoading] = useState(false);

    const handleUpload = async() => {
        if(!file) return;
        setStatus("Uploading...");
        setLoading(true);
        const res = await uploadDocument(patientId, file);
        if(res.ok) {
            setStatus("Upload successful!");
            onUploadSuccess();
        } else {
            setStatus("Upload failed.");
        }
        setLoading(false);
    }

    return(
        <div className="upload-container">
            <h2>Upload Document for Patient {patientId}</h2>
            <input type="file" onChange={(e) => setFile(e.target.files[0])} />
            <button onClick={handleUpload} disabled={!file || loading}>{loading ? "Uploading..." : "Upload"}</button>
            <p>{status}</p>
        </div>
    );
 }