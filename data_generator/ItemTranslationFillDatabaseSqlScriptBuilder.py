import pandas as pd
import uuid
from typing import Union
import datetime


class Language:
    def __init__(self, name, code):
        self.name = name
        self.code = code


plLanguage = Language("polish", 'pl')
enLanguage = Language("english", 'en')
deLanguage = Language("german", 'de')
esLanguage = Language("spanish", 'es')
itLanguage = Language("italian", 'it')

mainLanguage = enLanguage.name

languages = [
    plLanguage,
    enLanguage,
    deLanguage,
    esLanguage,
    itLanguage
]


def findFirstLanguageObject(var: Union[Language, None]):
    assert var is not None
    return next(
        (obj for obj in languages if obj.name == var.name)
    )


queryToCreateLanguage = """INSERT INTO "translation"."language" (iso_code, iso_name) VALUES('{}', '{}');"""
queryToFindLangaugeByIsoCode = """SELECT l.id FROM "translation"."language" l WHERE l.iso_code LIKE '{}'"""
queryToCreateTextContent = """INSERT INTO "translation".text_content (original_text, language_id) VALUES('{}', ({}))"""
queryToFindTextContentByOrigalName = """SELECT tc.id FROM "translation".text_content tc WHERE tc.original_text like '{}'"""
queryToCreateItem = """INSERT INTO item.item (uuid, "name", text_content_id) VALUES('{}', '{}', ({}));"""
queryToCreateTranslation = """INSERT INTO "translation"."translation" ("translation", text_content_id, language_id) VALUES('{}', ({}), ({}));"""

now = datetime.datetime.now().timestamp()

fileName = "scripts/item_data_{}.sql"

f = open(fileName.format(now), "a", encoding="utf-8")
for language in languages:
    f.write(queryToCreateLanguage.format(language.code, language.name) + '\n')


products = pd.read_csv("datasets/products.csv", delimiter=';')
products['polish'] = products['polish'].map(str.lower)
products['english'] = products['english'].map(str.lower)
products['german'] = products['german'].map(str.lower)
products['spanish'] = products['spanish'].map(str.lower)
products['italian'] = products['italian'].map(str.lower)

products = products.drop_duplicates('english', keep="last")

for product in products.index:
    originalNameTextContent = products[mainLanguage][product]
    f.write(queryToCreateTextContent.format(originalNameTextContent,
            queryToFindLangaugeByIsoCode.format(enLanguage.code))+";\n")
    f.write(queryToCreateItem.format(str(uuid.uuid4()), originalNameTextContent,
            queryToFindTextContentByOrigalName.format(originalNameTextContent))+"\n")
    for language in filter(lambda p: p.name != mainLanguage, languages):
        languageTemp = findFirstLanguageObject(language)
        translationItem = products[languageTemp.name][product].replace(
            "'", "''")
        insertQuery = queryToCreateTranslation.format(translationItem, queryToFindTextContentByOrigalName.format(
            originalNameTextContent), queryToFindLangaugeByIsoCode.format(languageTemp.code))+"\n"
        f.write(insertQuery)

f.close()
