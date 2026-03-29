import { useState } from "react";
import { deleteDocument, getPresignedUrl } from "../services/api";

export default function DocumentCard({ doc, onDelete }) {
    const [deleting, setDeleting] = useState(false);

    const handleDelete = async() => {
        setDeleting(true);
        await deleteDocument(doc.key);
        onDelete();
    }

     const handleView = async () => {
    const url = await getPresignedUrl(doc.key);
    window.open(url, "_blank");       // opens file in a new tab via pre-signed URL
  };

    return(
        <div className="doc-card">
            <div className="doc-info">
                <span className="doc-name">{doc.name}</span>
                <span className="doc-meta">{(doc.size / 1024).toFixed(1)} KB &nbsp;|&nbsp; {doc.lastModified}</span>
                </div>
            <div className="doc-actions">
                <button  className="btn-delete" onClick={handleDelete} disabled={deleting}>{deleting ? "Deleting..." : "Delete"}</button>
                <button className="btn-view" onClick={handleView}>View</button>
            </div>
        </div>
    )
}