import { useState } from "react";

export default function PatientSelector({ onSelect }) {
  const [input, setInput] = useState("");
  const [selected, setSelected] = useState(null);

  const handleSelect = () => {
    if (!input.trim()) return;
    setSelected(input.trim());
    onSelect(input.trim());           // lifts the patientId up to App
  };

  return (
    <div className="card">
      <h2>Select Patient</h2>
      <div className="row">
        <input
          type="text"
          placeholder="Enter Patient ID (e.g. P001)"
          value={input}
          onChange={(e) => setInput(e.target.value)}
        />
        <button onClick={handleSelect}>Load</button>
      </div>
      {selected && <p className="hint">Viewing records for: <strong>{selected}</strong></p>}
    </div>
  );
}