class CreateChatRooms < ActiveRecord::Migration[6.0]
  def change
    create_table :chat_rooms do |t|
      t.references :user, null: false, foreign_key: true
      t.string :name, null: false
      t.boolean :is_active, null: false, default: true
      t.boolean :is_private, null: false, default: false
      t.datetime :created_at, null: false
    end
  end
end
