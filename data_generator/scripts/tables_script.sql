CREATE SCHEMA IF NOT EXISTS item;
DROP TABLE item.item;

CREATE SCHEMA IF NOT EXISTS translation;
DROP TABLE "translation"."translation"; 
DROP TABLE "translation".text_content;
DROP TABLE "translation"."language";


CREATE TABLE item.item (
	uuid uuid NOT NULL,
	"name" varchar(50) NULL,
	text_content_id int8 NULL,
	CONSTRAINT item_pkey PRIMARY KEY (uuid)
);

CREATE TABLE "translation"."language" (
	id int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	iso_code varchar(2) NULL,
	iso_name varchar(15) NULL,
	CONSTRAINT language_pkey PRIMARY KEY (id)
);

CREATE TABLE "translation".text_content (
	id int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	original_text varchar(50) NULL,
	language_id int8 NULL,
	CONSTRAINT text_content_pkey PRIMARY KEY (id)
);

CREATE TABLE "translation"."translation" (
	id int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	"translation" varchar(50) NULL,
	text_content_id int8 NULL,
	language_id int8 NULL,
	CONSTRAINT translation_pkey PRIMARY KEY (id)
);


ALTER TABLE item.item ADD CONSTRAINT fk_item_item_translation_text_content_text_content_id FOREIGN KEY (text_content_id) REFERENCES "translation".text_content(id);
ALTER TABLE "translation".text_content ADD CONSTRAINT fk_translation_text_content_translation_language_language_id FOREIGN KEY (language_id) REFERENCES "translation"."language"(id);
ALTER TABLE "translation"."translation" ADD CONSTRAINT fk_translation_translation_translation_language_language_id FOREIGN KEY (language_id) REFERENCES "translation"."language"(id);
ALTER TABLE "translation"."translation" ADD CONSTRAINT fk_translation_translation_translation_text_content_text_content_id FOREIGN KEY (text_content_id) REFERENCES "translation".text_content(id);
