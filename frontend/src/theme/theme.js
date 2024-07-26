import { extendTheme } from "@chakra-ui/react";
import styles from "./styles";

const config = {
  initialColorMode: "dark",
  useSystemColorMode: false,
};

const theme = extendTheme({
  components: {
    Text: {
      baseStyle: {
        fontSize: "28px"
      }
    },
    Button: {
      sizes: {
        md: {
          fontSize: "28px"
        }
      },
    },
  },
  styles,
  config,
});

export default theme;
