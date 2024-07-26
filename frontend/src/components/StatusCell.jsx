import { Menu, MenuButton, MenuList, MenuItem } from '@chakra-ui/react'
import { useEffect, useState } from "react"

const StatusCell = ({ getValue, row, column, table, enums}) => {
    const initialValue = getValue()
    const [value, setValue] = useState(initialValue)
    const { updateData } = table.options.meta;

    return (
        <Menu isLazy offset={[0, 0]} autoSelect={false}>
            <MenuButton
                h="100%"
                w="100%"
                textAlign="center"
                bg={"transparent"}
                color="green.600"
                _hover={{ bg: 'gray.500' }}
                fontSize="28px"
            >   
                {value}
            </MenuButton>
            <MenuList>
                {enums[column.id].map((option, index) => (
                    <MenuItem
                        onClick={() => {
                            setValue(option);
                            updateData(row.index, column.id, option);
                        }}
                        key={index}
                    >
                        {option}
                    </MenuItem>
                ))}
            </MenuList>
        </Menu>
    );
};
export default StatusCell;