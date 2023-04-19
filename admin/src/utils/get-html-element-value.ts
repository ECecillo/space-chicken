function getHTMLInputElementValue(id: string): string {
  const input = document.getElementById(id) as HTMLInputElement | null;
  if (input !== null) return input.value;
}

function setHTMLInputElementValue(id: string, value: string): void {
  const input = document.getElementById(id) as HTMLInputElement | null;
  if (input != null) input.value = value;
}

export { getHTMLInputElementValue, setHTMLInputElementValue };
