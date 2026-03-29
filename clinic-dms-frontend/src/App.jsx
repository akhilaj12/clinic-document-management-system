import { useState } from 'react'
import UploadDocument from './components/UploadDocument'
import DocumentList from './components/DocumentList'
import PatientSelector from './components/PatientSelector'
import './App.css'
function App() {
  const [patientId, setPatientId] = useState("");
  const [refreshKey, setRefreshKey] = useState(0);

  const handleUploadSuccess = () => {
    setRefreshKey((prev) => prev + 1); // Trigger refresh of document list
  }


  return (
    <div className='app-container'>
      <h1>🏥 Clinic Document Manager</h1>

      {/* Step 1 — Pick a patient */}
      <PatientSelector onSelect={setPatientId} />

      {patientId && (
        <>
          <UploadDocument patientId={patientId} onUploadSuccess={handleUploadSuccess} />
          <DocumentList patientId={patientId} refreshKey={refreshKey} />
          </>
        )}
      
    </div>
  )
}

export default App
