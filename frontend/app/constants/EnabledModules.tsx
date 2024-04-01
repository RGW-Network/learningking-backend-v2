//Constants to determine which module to hide or display during deployment/release
export const BACKOFFICE_ENABLED = process.env.REACT_APP_BACKOFFICE_ENABLED === "true";
export const CLIENT_SEARCH_ENABLED = process.env.REACT_APP_CLIENT_SEARCH_ENABLED === "true";
export const NOTES_DASHBOARD_ENABLED = process.env.REACT_APP_NOTES_DASHBOARD_ENABLED === "true";
export const PAYMENTS_ENABLED = process.env.REACT_APP_PAYMENTS_ENABLED === "true";
export const INCIDENTS_ENABLED = process.env.REACT_APP_INCIDENTS_ENABLED === "true";
export const VETTING_ENABLED = process.env.REACT_APP_VETTING_ENABLED === "true";
export const SERVER_STATS_ENABLED = process.env.REACT_APP_SERVER_STATS_ENABLED === "true";
export const NOTIFICATIONS_ENABLED = process.env.REACT_APP_NOTIFICATIONS_ENABLED === "true";
export const MANUAL_ADVANCE_PAYMENTS_ENABLED = process.env.REACT_APP_MANUAL_ADVANCE_PAYMENTS_ENABLED === "true";
export const LEADS_GENERATION_ENABLED = process.env.REACT_APP_LEADS_GENERATION_ENABLED === "true";
