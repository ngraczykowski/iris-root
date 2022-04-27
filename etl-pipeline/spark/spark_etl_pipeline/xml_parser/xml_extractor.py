import re
from enum import Enum
from typing import List, Optional, Type

import lxml.etree

NUMBER_REGEX = re.compile("(0|([1-9][0-9]*))$")


class XMLFunction(Enum):
    extract_value = "extract_value"
    extract_string_elements_array = "extract_string_elements_array"
    extract_boolean_array = "extract_boolean_array"
    extract_boolean_value = "extract_boolean_value"


XMLFunctionType = Type[int]


class XMLExtractor:
    """Contains logic for extraction specific type values
    from xml document
    """

    def _convert_text_to_boolean(self, text: str) -> Optional[bool]:
        """Parameters
        ----------
        text : str
            [description]

        Returns
        -------
        Optional[bool]
            Returns boolean representation of the string. Or None as
            the field does not contain anything.
        """
        text = text.lower()
        if text == "true":
            return True
        if text == "false":
            return False
        return None

    def extract_boolean_value(self, doc: lxml.etree._Element, xpath: str) -> Optional[bool]:
        """Parameters
        ----------
        doc : lxml.etree._Element
        xpath : str

        Returns
        -------
        Optional[bool]
            Returns boolean representation of the string. Or None as
            the field does not contain anything.
        """
        text = self.extract_value(doc, xpath)
        if not text:
            return None
        if NUMBER_REGEX.match(text.strip()):
            return bool(int(text))
        return self._convert_text_to_boolean(text)

    def extract_value(self, doc: lxml.etree._Element, xpath: str) -> Optional[str]:
        """Parameters
        ----------
        doc : lxml.etree._Element
        xpath : str

        Returns
        -------
        Optional[str]
            Returns value from the doc. Or None as
            the field does not contain anything.
        """
        texts = self.extract_string_elements_array(doc, xpath)
        if texts and len(texts) == 1:
            return texts[0]
        return None

    def extract_boolean_array(self, doc: lxml.etree._Element, xpath: str) -> List[Optional[bool]]:
        """Parameters
        ----------
        doc : lxml.etree._Element
        xpath : str

        Returns
        -------
        List[Optional[bool]]
            List of boolean or none values.
        """
        texts = self.extract_string_elements_array(doc, xpath)
        return [self._convert_text_to_boolean(text) for text in texts]

    def extract_string_elements_array(
        self, doc: lxml.etree._Element, xpath: str
    ) -> List[Optional[str]]:
        """Parameters
        ----------
        doc : lxml.etree._Element
        xpath : str
        Returns
        -------
        List[Optional[str]]
            List of values. If the xpath
            is valid but not text, then [None] or a list of None will be returned.
            If the xpath doesn't not exist,
            then [] will be returned.
        """
        result = []
        if doc is not None:
            xpaths = xpath if isinstance(xpath, list) else [xpath]
            for xpath in xpaths:
                elements = self.__find_string_array(doc, xpath)
                for el in elements:
                    result.append(el)

        return result

    def __find_string_array(self, doc: lxml.etree._Element, xpath: str) -> List[Optional[str]]:
        """Parameters
        ----------
        doc : lxml.etree._Element
            [description]
        xpath : str
            Xpath that indicates the element.
            The xpath must not contain `text()`

            because return None is a requirement.

            E.g. doc.xpath("elem") returns a list of 2
            elements,e lement_1 has a text, element_2 doesn't.
            `doc.xpath("elem/text()")`` returns a list of
            text from element_1 only, but nothing additional information about
            element_2. In this case, `None` from element_2
            is required
        Returns
        -------
        List[Optional[str]]

        Raises
        ------
        ValueError
            If xpath contains `text()`
        """
        if "text()" in xpath:
            raise ValueError("The xpath contains text() function, remove it and try again")
        x_elements = doc.xpath(xpath)
        if x_elements:
            texts = self.__map_to_texts(x_elements)
            return texts
        return []

    def __map_to_texts(self, elements):
        elements_texts = [e.text for e in elements]
        return map(lambda x: x.strip() if x else None, elements_texts)
