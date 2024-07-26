const styles = {
  global: (props) => ({
    "html, body": {
      margin: "0",
      backgroundColor: props.colorMode === "dark" ? "gray.900" : "white",
      color: props.colorMode === "dark" ? "whiteAlpha.800" : "blackAlpha.800",
    },
    svg: {
      cursor: "pointer",
    },
    ".table": {
      width: "100%",
      borderCollapse: "collapse",
    },
    ".tr": {
      display: "table-row",
    },
    ".th, .td": {
      border: "1px solid",
      borderColor: props.colorMode === "dark" ? "#424242" : "#dcdcdc",
      display: "table-cell",
      textAlign: "center",
      verticalAlign: "middle",
    },
    ".th-colspan, .td-colspan": {
      display: "table-cell",
    },
    ".th": {
      backgroundColor: props.colorMode === "dark" ? "whiteAlpha.200" : "gray.100",
      color: props.colorMode === "dark" ? "gray.400" : "gray.800",
      padding: "0.5rem",
      fontWeight: "bold",
      fontSize: "xs",
      textTransform: "uppercase",
    },
    ".th-colspan": {
      backgroundColor: props.colorMode === "dark" ? "whiteAlpha.200" : "gray.100",
      color: props.colorMode === "dark" ? "gray.400" : "gray.800",
      padding: "0.5rem",
      fontWeight: "bold",
      fontSize: "xs",
      textTransform: "uppercase",
    },
    ".td > input": {
      textAlign: "center",
      padding: "0.1rem",
      backgroundColor: "transparent",
    },
    ".date-wrapper": {
      display: "flex",
      alignItems: "center",
      width: "100%",
      height: "100%",
    },
    ".resizer": {
      position: "absolute",
      opacity: 0,
      top: 0,
      right: 0,
      height: "100%",
      width: "5px",
      backgroundColor: "#27bbff",
      cursor: "col-resize",
      userSelect: "none",
      touchAction: "none",
      borderRadius: "6px",
    },
    ".resizer.isResizing": {
      backgroundColor: "#2eff31",
      opacity: 1,
    },
    "*:hover > .resizer": {
      opacity: 1,
    },
    // Styles for Chacra UI table
    ".chakra-table": {
      borderCollapse: "collapse",
      width: "100%",
    },
    ".chakra-table th, .chakra-table td": {
      border: "1px solid",
      borderColor: props.colorMode === "dark" ? "#424242" : "#dcdcdc",
      textAlign: "center",
      verticalAlign: "middle",
      padding: "0.5rem",
      fontSize: "28px",
    },
    ".chakra-table th": {
      backgroundColor: props.colorMode === "dark" ? "whiteAlpha.200" : "gray.100",
      color: props.colorMode === "dark" ? "gray.400" : "gray.800",
      fontWeight: "bold",
      fontSize: "28px",
      textTransform: "uppercase",

    },
  }),
};

export default styles;
