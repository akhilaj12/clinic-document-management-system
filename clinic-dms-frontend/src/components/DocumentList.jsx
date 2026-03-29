import { useEffect, useState } from "react";
import { getDocuments} from "../services/api";
import  DocumentCard from "./DocumentCard";

export default function DocumentList({ patientId, refreshKey }) {
  const [docs, setDocs] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => { loadDocs(); }, [patientId, refreshKey]);

  const loadDocs = async () => {
    setLoading(true);
    const data = await getDocuments(patientId);
    setDocs(data);
    setLoading(false);
  };

  if (loading) return <p className="hint">Loading documents...</p>;

  return (
    <div className="card">
      <h3>Documents for Patient: {patientId}</h3>
      {docs.length === 0 ? <p>No documents found.</p> : (
        <div className="doc-list">
            {docs.map((doc) => (<DocumentCard key={doc.key} doc={doc} onDelete={loadDocs} />))}
        </div>
      )}
    </div>
  );
}
