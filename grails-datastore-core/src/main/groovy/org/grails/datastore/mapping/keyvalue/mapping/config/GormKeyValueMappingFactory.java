/* Copyright (C) 2010 SpringSource
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.grails.datastore.mapping.keyvalue.mapping.config;

import org.grails.datastore.mapping.config.AbstractGormMappingFactory;
import org.grails.datastore.mapping.model.ClassMapping;
import org.grails.datastore.mapping.model.IdentityMapping;
import org.grails.datastore.mapping.model.PersistentEntity;
import org.grails.datastore.mapping.model.PersistentProperty;

/**
 * @author Graeme Rocher
 * @since 1.0
 */
public class GormKeyValueMappingFactory extends AbstractGormMappingFactory<Family, KeyValue> {

    private String keyspace;

    public GormKeyValueMappingFactory(String keyspace) {
        this.keyspace = keyspace;
    }

    @Override
    public Family createMappedForm(PersistentEntity entity) {
        Family family = super.createMappedForm(entity);
        if (family.getKeyspace() == null) {
            family.setKeyspace(keyspace);
        }
        if (family.getFamily() == null) {
            family.setFamily(entity.getName());
        }
        return family;
    }

    @Override
    public KeyValue createMappedForm(@SuppressWarnings("rawtypes") PersistentProperty mpp) {
        KeyValue kv = super.createMappedForm(mpp);
        if (kv.getKey() == null) {
            kv.setKey(mpp.getName());
        }
        return kv;
    }

    @Override
    protected Class<KeyValue> getPropertyMappedFormType() {
        return KeyValue.class;
    }

    @Override
    protected Class<Family> getEntityMappedFormType() {
        return Family.class;
    }

    @Override
    protected IdentityMapping getIdentityMappedForm(final ClassMapping classMapping, KeyValue property) {
        if(property != null) {
            final String name = property.getName();
            if(name != null) {
                final PersistentProperty idProperty = classMapping.getEntity().getPropertyByName(name);
                return new IdentityMapping() {
                    @Override
                    public String[] getIdentifierName() {
                        return new String[]{name};
                    }

                    @Override
                    public ClassMapping getClassMapping() {
                        return classMapping;
                    }

                    @Override
                    public Object getMappedForm() {
                        return idProperty.getMapping().getMappedForm();
                    }
                };
            }
        }
        return super.getIdentityMappedForm(classMapping, property);
    }
}
